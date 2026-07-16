package com.swp.autocarwash.system.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.system.dto.request.DashboardSummaryRequest;
import com.swp.autocarwash.system.dto.response.DashboardSummaryResponse;
import com.swp.autocarwash.system.service.DashboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
