package com.swp.autocarwash.system.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DashboardRepository {

    BigDecimal getTotalRevenue(
            LocalDate fromDate,
            LocalDate toDate,
            Integer stationId,
            Integer provinceId
    );

    Long getTotalBookings(
            LocalDate fromDate,
            LocalDate toDate,
            Integer stationId,
            Integer provinceId
    );

    Long getTotalCustomers(
            LocalDate fromDate,
            LocalDate toDate,
            Integer stationId,
            Integer provinceId
    );

}
