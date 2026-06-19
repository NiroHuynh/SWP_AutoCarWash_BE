package com.swp.autocarwash.booking.service.impl;

import com.swp.autocarwash.booking.calculator.SlotAvailabilityCalculator;
import com.swp.autocarwash.booking.dto.request.CreateBookingRequest;
import com.swp.autocarwash.booking.dto.response.CreateBookingResponse;
import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.BookingSlot;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.port.AddonServicePort;
import com.swp.autocarwash.booking.port.ServicePackagePort;
import com.swp.autocarwash.booking.port.VehiclePort;
import com.swp.autocarwash.booking.port.VoucherPort;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.repository.BookingSlotRepository;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 *  BookingServiceImpl dùng để xử lý logic liên quan đến việc tạo booking, bao gồm:
 *  - Validate thông tin xe
 *  - Tính toán giá dịch vụ
 *  - Xử lý voucher
 *
 * @author Phong
 * @version 1.0
 */

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSlotRepository slotRepository;
    private final ServicePackagePort servicePackagePort;
    private final AddonServicePort addonServicePort;
    private final VoucherPort voucherPort;
    private final VehiclePort vehiclePort;

    private final ModelMapper modelMapper;
    private final SlotAvailabilityCalculator slotCalculator = new SlotAvailabilityCalculator();

    @Override
    @Transactional
    public CreateBookingResponse createBooking(CreateBookingRequest request) {

        // 1. VALIDATE VEHICLE
        VehicleContract vehicle = vehiclePort.getById(request.getVehicleId());
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.VEHICLE_NOT_FOUND);
        }

        // 2. SERVICE PACKAGE
        var servicePackage = servicePackagePort.getById(request.getServicePackageId());

        // 3. ADDONS
        List<AddonServiceContract> addons = addonServicePort.getByIds(request.getAddonServiceIds());

        // 4. VALIDATE SLOTS (REAL BUSINESS RULE)
        List<BookingSlot> slots = slotRepository.findByIdIn(request.getSlotIds());

        if (slots.size() != request.getSlotIds().size()) {
            throw new BusinessException(ErrorCode.BOOKING_SLOT_NOT_AVAILABLE);
        }

        // check cùng station + liên tục + capacity
        boolean valid = slotCalculator.validateContinuousSlots(slots, servicePackage.getDurationMinutes());

        if (!valid) {
            throw new BusinessException(ErrorCode.BOOKING_INVALID_SLOT);
        }

        // 5. PRICE CALCULATION
        BigDecimal packagePrice = servicePackage.getBasePrice();

        BigDecimal addonPrice = addons.stream()
                .map(AddonServiceContract::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal subTotal = packagePrice.add(addonPrice);

        // 6. VOUCHER
        BigDecimal discount = BigDecimal.ZERO;

        if (request.getVoucherCode() != null) {

            var voucher = voucherPort.getDiscountPercent(request.getVoucherCode(), subTotal.intValue());

            if (!voucher.isValid()) {
                throw new BusinessException(ErrorCode.VOUCHER_INVALID);
            }

            discount = subTotal.multiply(
                    BigDecimal.valueOf(voucher.getDiscountPercentage())
                            .divide(BigDecimal.valueOf(100))
            );

            subTotal = subTotal.subtract(discount);
        }

        // 7. BUILD BOOKING ENTITY (FIXED)
        Booking booking = new Booking();
        booking.setVehicle(modelMapper.map(vehicle, Vehicle.class));
        booking.setServicePackage(modelMapper.map(servicePackage, ServicePackage.class));
        booking.setAppointmentDate(LocalDate.parse(request.getAppointmentDate()));
        booking.setStatus(BookingStatus.PENDING.toString());
        booking.setBookingType("ONLINE");
        booking.setTotalServiceAmount(packagePrice);
        booking.setTotalAddonAmount(addonPrice);
        booking.setVoucherDiscountAmount(discount);
        booking.setTotalAmount(subTotal);

        Booking saved = bookingRepository.save(booking);

        // 8. SLOT ALLOCATION (MISSING BEFORE)
        // TODO: insert booking_slot_allocation

        return CreateBookingResponse.builder()
                .bookingId(saved.getId())
                .status(saved.getStatus())
                .totalAmount(subTotal)
                .slotIds(request.getSlotIds())
                .build();
    }
}