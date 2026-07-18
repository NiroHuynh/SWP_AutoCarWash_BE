package com.swp.autocarwash.system.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class DashboardRevenueChartRequest {

    private LocalDate fromDate;

    private LocalDate toDate;

    private String groupBy;

    private Integer provinceId;

    private Integer stationId;

}
