package com.swp.autocarwash.system.service;

import com.swp.autocarwash.system.dto.request.DashboardRevenueChartRequest;
import com.swp.autocarwash.system.dto.request.DashboardSummaryRequest;
import com.swp.autocarwash.system.dto.request.DashboardTablesRequest;
import com.swp.autocarwash.system.dto.response.DashboardRevenueChartResponse;
import com.swp.autocarwash.system.dto.response.DashboardSummaryResponse;
import com.swp.autocarwash.system.dto.response.DashboardTablesResponse;

public interface DashboardService {

    DashboardSummaryResponse getDashboardSummary(
            DashboardSummaryRequest request
    );

    DashboardRevenueChartResponse getRevenueChart(
            DashboardRevenueChartRequest request
    );

    DashboardTablesResponse getDashboardTables(
            DashboardTablesRequest request
    );
}
