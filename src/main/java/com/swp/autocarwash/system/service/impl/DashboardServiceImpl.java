package com.swp.autocarwash.system.service.impl;

import com.swp.autocarwash.auth.entity.Role;
import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.UnauthorizedException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.station.repository.ProvinceRepository;
import com.swp.autocarwash.station.repository.StationRepository;
import com.swp.autocarwash.system.dto.request.DashboardSummaryRequest;
import com.swp.autocarwash.system.dto.response.DashboardSummaryResponse;
import com.swp.autocarwash.system.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final BookingRepository bookingRepository;
    private final ProvinceRepository provinceRepository;
    private final StationRepository stationRepository;
    private final SecurityUtils securityUtils;

    @Override
    public DashboardSummaryResponse getDashboardSummary(DashboardSummaryRequest request) {

        validateRequest(request);

        Integer stationId = null;
        Integer provinceId = null;

        if (securityUtils.hasRole("ROLE_ADMIN")) {

            if (request.getProvinceId() != null) {

                provinceRepository.findById(request.getProvinceId())
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.PROVINCE_NOT_FOUND));

                provinceId = request.getProvinceId();

            } else if (request.getStationId() != null) {

                stationRepository.findById(request.getStationId())
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.STATION_NOT_FOUND));

                stationId = request.getStationId();
            }

        } else if (securityUtils.hasRole("ROLE_STAFF")) {

            stationId = securityUtils.getEmployee()
                    .getStation()
                    .getId();

        } else {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        BigDecimal totalRevenue;
        Long totalBookings;
        Long totalCustomers;

        if (stationId != null) {

            totalRevenue = bookingRepository.getTotalRevenueByStation(
                    request.getFromDate(),
                    request.getToDate(),
                    stationId);

            totalBookings = bookingRepository.getTotalBookingsByStation(
                    request.getFromDate(),
                    request.getToDate(),
                    stationId);

            totalCustomers = bookingRepository.getTotalCustomersByStation(
                    request.getFromDate(),
                    request.getToDate(),
                    stationId);

        } else if (provinceId != null) {

            totalRevenue = bookingRepository.getTotalRevenueByProvince(
                    request.getFromDate(),
                    request.getToDate(),
                    provinceId);

            totalBookings = bookingRepository.getTotalBookingsByProvince(
                    request.getFromDate(),
                    request.getToDate(),
                    provinceId);

            totalCustomers = bookingRepository.getTotalCustomersByProvince(
                    request.getFromDate(),
                    request.getToDate(),
                    provinceId);

        } else {

            totalRevenue = bookingRepository.getTotalRevenue(
                    request.getFromDate(),
                    request.getToDate());

            totalBookings = bookingRepository.getTotalBookings(
                    request.getFromDate(),
                    request.getToDate());

            totalCustomers = bookingRepository.getTotalCustomers(
                    request.getFromDate(),
                    request.getToDate());
        }

        return DashboardSummaryResponse.builder()
                .totalRevenue(totalRevenue)
                .totalBookings(totalBookings)
                .totalCustomers(totalCustomers)
                .build();
    }

    private void validateRequest(DashboardSummaryRequest request) {

        if (request.getFromDate() == null || request.getToDate() == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        if (request.getFromDate().isAfter(request.getToDate())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        if (request.getProvinceId() != null
                && request.getStationId() != null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
    }
}
