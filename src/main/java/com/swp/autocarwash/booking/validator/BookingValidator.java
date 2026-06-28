package com.swp.autocarwash.booking.validator;

import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.booking.calculator.SlotAvailabilityCalculator;
import com.swp.autocarwash.booking.dto.request.CreateBookingRequest;
import com.swp.autocarwash.booking.entity.BookingSlot;
import com.swp.autocarwash.booking.port.*;
import com.swp.autocarwash.booking.repository.BookingSlotAllocationRepository;
import com.swp.autocarwash.booking.repository.BookingSlotRepository;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingValidator {

    private final VehiclePort vehiclePort;
    private final ServicePackagePort servicePackagePort;
    private final BookingSlotRepository slotRepository;
    private final AddonServicePort addonServicePort;
    private final CustomerPort customerPort;
    private final BookingSlotAllocationRepository bookingSlotAllocationRepository;
    private final BookingSlotRepository bookingSlotRepository;

    private final SecurityUtils securityUtils;
    private final SlotAvailabilityCalculator slotCalculator;

    public void validateCreateBooking(CreateBookingRequest request) {

        // 1. VALIDATE CUSTOMER
        CustomerContract customer = validateCustomer();


        // 2. VALIDATE VEHICLE
        validateVehicle(request.getVehicleId(), customer.getId());


        // 3. SERVICE PACKAGE
        var servicePackage = servicePackagePort.getById(request.getServicePackageId());


        // 4. VALIDATE SLOTS
        List<BookingSlot> slots = validateSlots(request.getVehicleId(), request.getSlotIds());


        // 5. VALIDATE SLOT CONDITION
        validateSlotDuration(slots, servicePackage.getDurationMinutes(), request.getAddonServiceIds());


        // 6. SET APPOINTMENT DATE
        request.setAppointmentDate(slots.get(0).getDate().toString());
    }

    private CustomerContract validateCustomer() {

        Long userId = securityUtils.getCurrentUserId();

        CustomerContract customer =
                customerPort.getCustomerByUserId(userId);


        if (customer == null) {
            throw new BusinessException(
                    ErrorCode.CUSTOMER_NOT_FOUND
            );
        }


        return customer;
    }

    private void validateVehicle(
            Long vehicleId,
            Long customerId
    ) {

        VehicleContract vehicle =
                vehiclePort.getById(vehicleId);


        if (vehicle == null) {
            throw new BusinessException(
                    ErrorCode.VEHICLE_NOT_FOUND
            );
        }


        vehiclePort.validateVehicleOwnership(
                vehicleId,
                customerId
        );
    }

    private List<BookingSlot> validateSlots(
            Long vehicleId,
            List<Long> slotIds
    ) {
//        kiểm tra hết hạn
        validateSlotAlreadyBooked(vehicleId,slotIds);

//        kiểm tra đã booking slot này trước đó chưa
        validateSlotAlreadyBooked(vehicleId, slotIds);

        List<BookingSlot> slots =
                slotRepository.findByIdIn(slotIds);


        if (slots.size() != slotIds.size()) {
            throw new BusinessException(
                    ErrorCode.BOOKING_SLOT_NOT_AVAILABLE
            );
        }


        return slots;
    }

    private void validateSlotDuration(
            List<BookingSlot> slots,
            Integer serviceDurationMinutes,
            List<Integer> addonIds
    ) {


        Integer addonDurationMinutes =
                addonIds != null && !addonIds.isEmpty()
                        ? addonServicePort.getTotalDuration(addonIds)
                        : 0;



        boolean valid =
                slotCalculator.validateContinuousSlots(
                        slots,
                        serviceDurationMinutes + addonDurationMinutes
                );


        if (!valid) {
            throw new BusinessException(
                    ErrorCode.BOOKING_INVALID_SLOT
            );
        }
    }

    private void validateSlotAlreadyBooked(
            Long vehicleId,
            List<Long> slotIds
    ) {

        boolean conflict =
                bookingSlotAllocationRepository
                        .existsConflictSlot(vehicleId, slotIds);


        if (conflict) {
            throw new BusinessException(
                    ErrorCode.BOOKING_SLOT_ALREADY_USED
            );
        }
    }

    private void validateSlotNotExpired(
            List<Long> slotIds
    ) {

        if (slotIds == null || slotIds.isEmpty()) {
            return;
        }

        LocalDate today = LocalDate.now(
                ZoneId.of("Asia/Ho_Chi_Minh")
        );

        LocalTime now = LocalTime.now(
                ZoneId.of("Asia/Ho_Chi_Minh")
        );


        boolean expired =
                bookingSlotRepository.existsExpiredSlot(
                        slotIds,
                        today,
                        now
                );


        if (expired) {
            throw new BusinessException(ErrorCode.BOOKING_SLOT_EXPIRED);
        }
    }

}
