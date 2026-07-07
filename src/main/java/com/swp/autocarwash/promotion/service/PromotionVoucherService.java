package com.swp.autocarwash.promotion.service;

import com.swp.autocarwash.promotion.dto.request.CreatePromotionVoucherRequest;
import com.swp.autocarwash.promotion.dto.response.CreatePromotionVoucherResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionTargetResponse;

import java.util.List;

public interface PromotionVoucherService {

    CreatePromotionVoucherResponse createPromotionOrVoucher(CreatePromotionVoucherRequest request);
    List<PromotionTargetResponse> getAllPromotionTargets();
}
