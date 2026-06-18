package com.swp.autocarwash.booking.service.impl;

import com.swp.autocarwash.booking.dto.response.BookingContextResponse;
import com.swp.autocarwash.booking.mapper.BookingMapper;
import com.swp.autocarwash.booking.port.*;
import com.swp.autocarwash.booking.service.BookingContextService;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.common.contract.loyalty.CustomerTierContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * BookingContextServiceImpl dung de lay thong tin can thiet cho viec tao booking
 *
 * @author Phong
 * @version 1.0
 */

@Service
@RequiredArgsConstructor
public class BookingContextServiceImpl implements BookingContextService {

    private final VehiclePort vehiclePort;
    private final ServicePackagePort servicePackagePort;
    private final AddonServicePort addonServicePort;
    private final VoucherPort voucherPort;
    private final LoyaltyPort loyaltyPort;
    private final BookingMapper bookingMapper;

    /**
     * Lấy customerId từ JWT hoặc session
     */
    private Integer getCurrentCustomerId() {

        // CASE 1: JWT
        // return jwtUtil.getUserId();

        // CASE 2: SESSION
        // return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return 1; // mock
    }

    @Override
    public BookingContextResponse getBookingContext(Integer stationId) {

        Integer customerId = getCurrentCustomerId();

        List<VehicleContract> vehicles = vehiclePort.getVehiclesByCustomer(customerId);
//        List<VehicleContract> vehicles = null;

        if (vehicles == null || vehicles.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_VEHICLE_REGISTERED);
        }

        // AC: compute booking window theo tier
        LocalDate now = LocalDate.now();
        int limitDays = resolveTierLimitDays(customerId);

        BookingContextResponse.BookingWindowDTO window =
                BookingContextResponse.BookingWindowDTO.builder()
                        .minDate(now)
                        .maxDate(now.plusDays(limitDays))
                        .build();

        return BookingContextResponse.builder()
                .stationId(stationId)
                .bookingWindow(window)
                .vehicles(vehicles.stream().map(bookingMapper::toVehicleDTO).toList())
                .servicePackages(servicePackagePort.getAllPackages()
                        .stream().map(bookingMapper::toPackage).toList())
                .addonServices(addonServicePort.getAllAddons()
                        .stream().map(bookingMapper::toAddon).toList())
                .vouchers(voucherPort.getValidVouchers(customerId)
                        .stream().map(bookingMapper::toVoucher).toList())
                .build();
    }

    /**
     * AC logic: tier → số ngày booking window
     */
    private int resolveTierLimitDays(Integer customerId) {

        CustomerTierContract tier = loyaltyPort.getCustomerTier(customerId);
        return tier.getBookingWindowDays();

    }
}
