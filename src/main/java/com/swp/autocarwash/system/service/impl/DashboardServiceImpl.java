package com.swp.autocarwash.system.service.impl;

import com.swp.autocarwash.auth.entity.Role;
import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.UnauthorizedException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.staff.entity.Staff;
import com.swp.autocarwash.station.entity.Station;
import com.swp.autocarwash.station.repository.ProvinceRepository;
import com.swp.autocarwash.station.repository.StationRepository;
import com.swp.autocarwash.system.dto.request.DashboardGroupBy;
import com.swp.autocarwash.system.dto.request.DashboardRevenueChartRequest;
import com.swp.autocarwash.system.dto.request.DashboardSummaryRequest;
import com.swp.autocarwash.system.dto.request.DashboardTablesRequest;
import com.swp.autocarwash.system.dto.response.*;
import com.swp.autocarwash.system.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final BookingRepository bookingRepository;
    private final ProvinceRepository provinceRepository;
    private final StationRepository stationRepository;
    private final CustomerRepository customerRepository;
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

    @Override
    public DashboardRevenueChartResponse getRevenueChart(
            DashboardRevenueChartRequest request
    ) {

        // ===== Validate =====

        if (request.getFromDate() == null
                || request.getToDate() == null
                || request.getGroupBy() == null) {

            throw new BusinessException(ErrorCode.DASHBOARD_REQUIRED_FIELDS_MISSING);
        }

        if (request.getFromDate().isAfter(request.getToDate())) {
            throw new BusinessException(ErrorCode.DASHBOARD_INVALID_DATE_RANGE);
        }

        if (request.getProvinceId() != null
                && request.getStationId() != null) {

            throw new BusinessException(
                    ErrorCode.DASHBOARD_CANNOT_FILTER_BOTH_PROVINCE_AND_STATION
            );
        }

        DashboardGroupBy groupBy;

        try {
            groupBy = DashboardGroupBy.valueOf(
                    request.getGroupBy().toUpperCase()
            );
        } catch (Exception e) {
            throw new BusinessException(
                    ErrorCode.DASHBOARD_INVALID_GROUP_BY
            );
        }

        if (groupBy == DashboardGroupBy.HOUR
                && !request.getFromDate().equals(request.getToDate())) {

            throw new BusinessException(
                    ErrorCode.DASHBOARD_HOUR_REQUIRES_SAME_DATE
            );
        }

        Integer stationId = null;
        Integer provinceId = null;

        // ===== STAFF =====

        if (securityUtils.hasRole("ROLE_STAFF")) {

            Staff staff = securityUtils.getEmployee();

            stationId = staff.getStation().getId();

        }

        // ===== ADMIN =====

        else if (securityUtils.hasRole("ROLE_ADMIN")) {

            if (request.getStationId() != null) {

                Station station = stationRepository
                        .findById(request.getStationId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.STATION_NOT_FOUND
                                ));

                stationId = station.getId();

            } else if (request.getProvinceId() != null) {

                provinceRepository
                        .findById(request.getProvinceId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.PROVINCE_NOT_FOUND
                                ));

                provinceId = request.getProvinceId();
            }

        }

        else {

            throw new UnauthorizedException(
                    ErrorCode.UNAUTHORIZED_ACCESS
            );
        }

        // ===== Dispatch =====

        return switch (groupBy) {

            case MONTH -> buildMonthChart(
                    request.getFromDate(),
                    request.getToDate(),
                    stationId,
                    provinceId
            );

            case DAY -> buildDayChart(
                    request.getFromDate(),
                    request.getToDate(),
                    stationId,
                    provinceId
            );

            case QUARTER -> buildQuarterChart(
                    request.getFromDate(),
                    request.getToDate(),
                    stationId,
                    provinceId
            );

            case HOUR -> buildHourChart(
                    request.getFromDate(),
                    stationId,
                    provinceId
            );
        };
    }

    private void validateRequest(DashboardRevenueChartRequest request) {

        if (request.getFromDate() == null
                || request.getToDate() == null) {
            throw new BusinessException(
                    ErrorCode.FROM_DATE_AND_TO_DATE_REQUIRED);
        }

        if (request.getGroupBy() == null) {
            throw new BusinessException(
                    ErrorCode.GROUP_BY_REQUIRED);
        }

        if (request.getFromDate().isAfter(request.getToDate())) {
            throw new BusinessException(
                    ErrorCode.INVALID_DATE_RANGE);
        }

        if (DashboardGroupBy.valueOf(request.getGroupBy()) == DashboardGroupBy.HOUR
                && !request.getFromDate().equals(request.getToDate())) {

            throw new BusinessException(
                    ErrorCode.HOUR_GROUPBY_REQUIRES_SAME_DATE);
        }

        if (request.getProvinceId() != null
                && request.getStationId() != null) {

            throw new BusinessException(
                    ErrorCode.CANNOT_FILTER_BY_BOTH_PROVINCE_AND_STATION);
        }
    }

    private static final List<String> MONTH_LABELS = List.of(
            "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
            "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
    );

    private DashboardRevenueChartResponse buildMonthChart(
            LocalDate fromDate,
            LocalDate toDate,
            Integer stationId,
            Integer provinceId
    ) {

        List<RevenueChartProjection> projections =
                bookingRepository.getRevenueByMonth(
                        fromDate,
                        toDate,
                        stationId,
                        provinceId
                );

        BigDecimal revenueTotal =
                bookingRepository.getRevenueTotal(
                        fromDate,
                        toDate,
                        stationId,
                        provinceId
                );

        Map<Integer, BigDecimal> revenueMap = toMap(projections);

        List<RevenueChartItemResponse> chartData = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {

            chartData.add(
                    RevenueChartItemResponse.builder()
                            .label(MONTH_LABELS.get(month - 1))
                            .value(
                                    revenueMap.getOrDefault(
                                            month,
                                            BigDecimal.ZERO
                                    )
                            )
                            .build()
            );
        }

        return buildResponse(revenueTotal, chartData);
    }

    private DashboardRevenueChartResponse buildResponse(
            BigDecimal revenueTotal,
            List<RevenueChartItemResponse> chartData
    ) {

        return DashboardRevenueChartResponse.builder()
                .revenueTotal(revenueTotal)
                .chartData(chartData)
                .build();
    }
    private Map<Integer, BigDecimal> toMap(
            List<RevenueChartProjection> projections
    ) {

        return projections.stream()
                .collect(Collectors.toMap(
                        RevenueChartProjection::getLabel,
                        RevenueChartProjection::getValue
                ));
    }

    private BigDecimal toBigDecimal(BigDecimal value) {
        return value == null
                ? BigDecimal.ZERO
                : value;
    }

    private DashboardRevenueChartResponse buildDayChart(
            LocalDate fromDate,
            LocalDate toDate,
            Integer stationId,
            Integer provinceId
    ) {

        List<RevenueChartProjection> projections =
                bookingRepository.getRevenueByDay(
                        fromDate,
                        toDate,
                        stationId,
                        provinceId
                );

        BigDecimal revenueTotal =
                bookingRepository.getRevenueTotal(
                        fromDate,
                        toDate,
                        stationId,
                        provinceId
                );

        Map<Integer, BigDecimal> revenueMap = toMap(projections);

        List<RevenueChartItemResponse> chartData = new ArrayList<>();

        LocalDate current = fromDate;

        while (!current.isAfter(toDate)) {

            int day = current.getDayOfMonth();

            chartData.add(
                    RevenueChartItemResponse.builder()
                            .label(String.format("%02d", day))
                            .value(
                                    revenueMap.getOrDefault(
                                            day,
                                            BigDecimal.ZERO
                                    )
                            )
                            .build()
            );

            current = current.plusDays(1);
        }

        return buildResponse(revenueTotal, chartData);
    }

    private DashboardRevenueChartResponse buildQuarterChart(
            LocalDate fromDate,
            LocalDate toDate,
            Integer stationId,
            Integer provinceId
    ) {

        List<RevenueChartProjection> projections =
                bookingRepository.getRevenueByQuarter(
                        fromDate,
                        toDate,
                        stationId,
                        provinceId
                );

        BigDecimal revenueTotal =
                bookingRepository.getRevenueTotal(
                        fromDate,
                        toDate,
                        stationId,
                        provinceId
                );

        Map<Integer, BigDecimal> revenueMap = toMap(projections);

        List<RevenueChartItemResponse> chartData = new ArrayList<>();

        for (int quarter = 1; quarter <= 4; quarter++) {

            chartData.add(
                    RevenueChartItemResponse.builder()
                            .label("Q" + quarter)
                            .value(
                                    revenueMap.getOrDefault(
                                            quarter,
                                            BigDecimal.ZERO
                                    )
                            )
                            .build()
            );
        }

        return buildResponse(revenueTotal, chartData);
    }

    private DashboardRevenueChartResponse buildHourChart(
            LocalDate date,
            Integer stationId,
            Integer provinceId
    ) {

        List<RevenueChartProjection> projections =
                bookingRepository.getRevenueByHour(
                        date,
                        stationId,
                        provinceId
                );

        BigDecimal revenueTotal =
                bookingRepository.getRevenueTotal(
                        date,
                        date,
                        stationId,
                        provinceId
                );

        Map<Integer, BigDecimal> revenueMap = toMap(projections);

        List<RevenueChartItemResponse> chartData = new ArrayList<>();

        for (int hour = 8; hour <= 21; hour++) {

            chartData.add(
                    RevenueChartItemResponse.builder()
                            .label(String.format("%02d:00", hour))
                            .value(
                                    revenueMap.getOrDefault(
                                            hour,
                                            BigDecimal.ZERO
                                    )
                            )
                            .build()
            );
        }

        return buildResponse(revenueTotal, chartData);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardTablesResponse getDashboardTables(
            DashboardTablesRequest request
    ) {

        // ===== Validate =====

        if (request.getFromDate() == null
                || request.getToDate() == null) {

            throw new BusinessException(
                    ErrorCode.DASHBOARD_TABLE_REQUIRED_FIELDS
            );
        }

        if (request.getFromDate().isAfter(request.getToDate())) {

            throw new BusinessException(
                    ErrorCode.DASHBOARD_TABLE_INVALID_DATE_RANGE
            );
        }

        if (request.getProvinceId() != null
                && request.getStationId() != null) {

            throw new BusinessException(
                    ErrorCode.DASHBOARD_TABLE_CANNOT_FILTER_BOTH_PROVINCE_AND_STATION
            );
        }

        Integer stationId = null;
        Integer provinceId = null;

        // ===== STAFF =====

        if (securityUtils.hasRole("ROLE_STAFF")) {

            stationId = securityUtils
                    .getEmployee()
                    .getStation()
                    .getId();

        }

        // ===== ADMIN =====

        else if (securityUtils.hasRole("ROLE_ADMIN")) {

            if (request.getStationId() != null) {

                if (!stationRepository.existsById(request.getStationId())) {
                    throw new BusinessException(
                            ErrorCode.STATION_NOT_FOUND
                    );
                }

                stationId = request.getStationId();

            } else if (request.getProvinceId() != null) {

                if (!provinceRepository.existsById(request.getProvinceId())) {
                    throw new BusinessException(
                            ErrorCode.PROVINCE_NOT_FOUND
                    );
                }

                provinceId = request.getProvinceId();
            }

        }

        // ===== Unauthorized =====

        else {

            throw new UnauthorizedException(
                    ErrorCode.UNAUTHORIZED_ACCESS
            );
        }

        // ===== Package Stats =====

        List<PackageStatResponse> packageStats =
                buildPackageStats(
                        request.getFromDate(),
                        request.getToDate(),
                        stationId,
                        provinceId
                );

        // ===== Tier Stats =====

        List<TierStatResponse> tierStats =
                customerRepository
                        .getTierStatistics()
                        .stream()
                        .map(item ->
                                TierStatResponse.builder()
                                        .tier(item.getTier())
                                        .customerCount(item.getCustomerCount())
                                        .build()
                        )
                        .toList();

        return DashboardTablesResponse.builder()
                .packageStats(packageStats)
                .tierStats(tierStats)
                .build();
    }

    private List<PackageStatResponse> buildPackageStats(
            LocalDate fromDate,
            LocalDate toDate,
            Integer stationId,
            Integer provinceId
    ) {

        List<PackageStatProjection> projections =
                bookingRepository.getPackageStatistics(
                        fromDate,
                        toDate,
                        stationId,
                        provinceId
                );

        if (projections.isEmpty()) {
            return Collections.emptyList();
        }

        long totalBooking =
                bookingRepository.getTotalBookingForPackageStatistic(
                        fromDate,
                        toDate,
                        stationId,
                        provinceId
                );

        return projections.stream()
                .map(item -> {

                    int percentage = totalBooking == 0
                            ? 0
                            : (int) Math.round(
                            item.getBookingCount() * 100.0 / totalBooking
                    );

                    return PackageStatResponse.builder()
                            .packageName(item.getPackageName())
                            .bookingCount(item.getBookingCount())
                            .percentage(percentage)
                            .build();
                })
                .toList();
    }
}
