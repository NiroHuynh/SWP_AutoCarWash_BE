package com.swp.autocarwash.promotion.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.promotion.dto.request.CreatePromotionVoucherRequest;
import com.swp.autocarwash.promotion.dto.request.UpdatePromotionRequest;
import com.swp.autocarwash.promotion.dto.request.UpdateVoucherRequest;
import com.swp.autocarwash.promotion.dto.response.CreatePromotionVoucherResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionBranchSummaryResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionDashboardListViewResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionTargetResponse;
import com.swp.autocarwash.promotion.service.PromotionVoucherService;
import jakarta.validation.Valid;
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

    /**
     * API-02-01: Lấy số liệu tổng hợp chiến dịch áp dụng theo từng chi nhánh (AC01)
     * URL: GET /api/admin/promotions/branches-summary?status=ACTIVE
     */
    @GetMapping("/branches-summary")
    public ResponseEntity<ApiResponse<List<PromotionBranchSummaryResponse>>> getBranchPromotionSummary(
            @RequestParam(value = "status", required = false, defaultValue = "ACTIVE") String status
    ) {
        // 1. Gọi Service lấy danh sách số liệu thô từ câu query GROUP BY dưới DB
        List<PromotionBranchSummaryResponse> response = promotionVoucherService.getBranchPromotionSummary(status);

        // 2. Đóng gói JSON trả về đồng bộ form mẫu thành công
        return ResponseEntity.ok(ApiResponse.success("Get the branch summary data table successfully!", response));
    }

    /**
     * API-02-02: Lấy danh sách khuyến mãi hỗn hợp (Chiến dịch + Voucher lẻ) không phân trang (AC02)
     * URL: GET /api/admin/promotions?stationId=1&status=ACTIVE
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PromotionDashboardListViewResponse>>> getPromotionDashboardList(
            @RequestParam(value = "stationId", required = false) Integer stationId,
            @RequestParam(value = "status", required = false, defaultValue = "ACTIVE") String status
    ) {
        // 1. Gọi Service thực hiện thuật toán gộp đa hình (Union) và lọc dữ liệu bằng vòng lặp truyền thống
        List<PromotionDashboardListViewResponse> response = promotionVoucherService.getPromotionDashboardList(stationId, status);

        // 2. Đóng gói JSON trả về mảng danh sách trực tiếp cho FE dễ map vòng lặp render
        return ResponseEntity.ok(ApiResponse.success("Successfully retrieved the list of promotions!" , response));
    }

    @PutMapping("/{promotionId}")
    public ResponseEntity<ApiResponse<?>> updatePromotion(
            @PathVariable Integer promotionId,
            @Valid @RequestBody UpdatePromotionRequest request) {

        promotionVoucherService.updatePromotion(promotionId, request);
        return ResponseEntity.ok(ApiResponse.success("The campaign configuration and applicable affiliates have been successfully updated!", null));
    }

    @PutMapping("/vouchers/{voucherId}")
    public ResponseEntity<?> updateVoucherFinancialRules(
            @PathVariable Integer voucherId,
            @Valid @RequestBody UpdateVoucherRequest request) {

        promotionVoucherService.updateVoucherFinancialRules(voucherId, request);
        return ResponseEntity.ok(ApiResponse.success("Voucher configurations updated successfully!", null));
    }
}
