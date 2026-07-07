package com.swp.autocarwash.promotion.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.promotion.dto.request.CreatePromotionVoucherRequest;
import com.swp.autocarwash.promotion.dto.response.CreatePromotionVoucherResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionTargetResponse;
import com.swp.autocarwash.promotion.service.PromotionVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/promotions")
@RequiredArgsConstructor
public class PromotionVoucherController {

    private final PromotionVoucherService promotionVoucherService;

    @PostMapping("/config")
    public ResponseEntity<ApiResponse<CreatePromotionVoucherResponse>> configurePromotionOrVoucher(@RequestBody CreatePromotionVoucherRequest request) {

        promotionVoucherService.createPromotionOrVoucher(request);

        // Gọi service hứng cục data trả về
        CreatePromotionVoucherResponse responseData = promotionVoucherService.createPromotionOrVoucher(request);

        String message = "Configuration processed successfully!";
        if (request.getConfigMode() == 1) message = "The direct discount campaign configuration was successful!";
        if (request.getConfigMode() == 2) message = "Campaign created and voucher code successfully linked!";
        if (request.getConfigMode() == 3) message = "Independent voucher code created successfully!";

        return ResponseEntity.ok(ApiResponse.success(message, responseData));
    }

    @GetMapping("/targets")
    public ResponseEntity<ApiResponse<List<PromotionTargetResponse>>> getPromotionTargets() {
        List<PromotionTargetResponse> targets = promotionVoucherService.getAllPromotionTargets();
        return ResponseEntity.ok(ApiResponse.success("List of promotion target", targets));
    }
}
