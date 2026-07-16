package com.swp.autocarwash.system.service;

import com.swp.autocarwash.system.dto.request.DashboardSummaryRequest;
import com.swp.autocarwash.system.dto.response.DashboardSummaryResponse;

public interface DashboardService {

    DashboardSummaryResponse getDashboardSummary(
            DashboardSummaryRequest request
    );

}
