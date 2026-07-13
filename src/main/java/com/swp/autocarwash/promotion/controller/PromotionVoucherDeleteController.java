package com.swp.autocarwash.promotion.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.promotion.service.PromotionVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class PromotionVoucherDeleteController {

    private final PromotionVoucherService promotionVoucherService;

    // API xóa mềm Chiến dịch (Chế độ 1 & 2)
    @PatchMapping("/promotions/{promotionId}/soft-delete")
    public ResponseEntity<ApiResponse<?>> softDeletePromotion(@PathVariable Integer promotionId) {

        promotionVoucherService.softDeletePromotion(promotionId);
        return ResponseEntity.ok(ApiResponse.success("The campaign and all associated voucher codes have been successfully deactivated and removed!", null));
    }

    // API xóa mềm Voucher lẻ (Chế độ 3)
    @PatchMapping("/vouchers/{voucherId}/soft-delete")
    public ResponseEntity<ApiResponse<?>> softDeleteStandaloneVoucher(@PathVariable Long voucherId) {

        promotionVoucherService.softDeleteStandaloneVoucher(voucherId);
        return ResponseEntity.ok(ApiResponse.success("Individual discount codes have been successfully disabled and soft-deleted!", null));
    }
}
