package com.swp.autocarwash.system.controller;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.system.dto.request.DashboardRevenueChartRequest;
import com.swp.autocarwash.system.dto.request.DashboardSummaryRequest;
import com.swp.autocarwash.system.dto.request.DashboardTablesRequest;
import com.swp.autocarwash.system.dto.response.DashboardRevenueChartResponse;
import com.swp.autocarwash.system.dto.response.DashboardSummaryResponse;
import com.swp.autocarwash.system.dto.response.DashboardTablesResponse;
import com.swp.autocarwash.system.service.DashboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> getDashboardSummary(
            @Valid @ModelAttribute DashboardSummaryRequest request) {

        return ApiResponse.success(
                "Get dashboard summary successfully",
                dashboardService.getDashboardSummary(request)
        );
    }

    @GetMapping("/revenue-chart")
    public ApiResponse<DashboardRevenueChartResponse> getRevenueChart(
            @ModelAttribute DashboardRevenueChartRequest request
    ) {


        return ApiResponse.success(
                "Get revenue chart successfully",
                dashboardService.getRevenueChart(request)
        );
    }

    @GetMapping("/tables")
    public ApiResponse<DashboardTablesResponse> getDashboardTables(
            @ModelAttribute DashboardTablesRequest request
    ) {

        return ApiResponse.success(
                "Get dashboard tables successfully",
                dashboardService.getDashboardTables(request)
        );
    }

}
