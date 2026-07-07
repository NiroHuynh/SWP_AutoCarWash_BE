package com.swp.autocarwash.promotion.service;

import com.swp.autocarwash.promotion.dto.request.CreatePromotionVoucherRequest;
import com.swp.autocarwash.promotion.dto.response.CreatePromotionVoucherResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionBranchSummaryResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionDashboardListViewResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionTargetResponse;

import java.util.List;

public interface PromotionVoucherService {

    CreatePromotionVoucherResponse createPromotionOrVoucher(CreatePromotionVoucherRequest request);
    List<PromotionTargetResponse> getAllPromotionTargets();
    List<PromotionBranchSummaryResponse> getBranchPromotionSummary(String status);
    List<PromotionDashboardListViewResponse> getPromotionDashboardList(Integer stationId, String status);
}
