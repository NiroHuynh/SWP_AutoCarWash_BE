package com.swp.autocarwash.system.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DashboardSummaryRequest {

    @NotNull(message = "fromDate is required")
    private LocalDate fromDate;

    @NotNull(message = "toDate is required")
    private LocalDate toDate;

    private Integer provinceId;

    private Integer stationId;
}
