package com.swp.autocarwash.refund.controller;

import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.refund.dto.request.ConfirmRefundRequest;
import com.swp.autocarwash.refund.dto.request.CreateRefundRequest;
import com.swp.autocarwash.refund.dto.response.AccountLookupResponse;
import com.swp.autocarwash.refund.dto.response.BankOptionResponse;
import com.swp.autocarwash.refund.dto.response.DepositAmountResponse;
import com.swp.autocarwash.refund.dto.response.RefundDetailResponse;
import com.swp.autocarwash.refund.dto.response.RefundListPageResponse;
import com.swp.autocarwash.refund.dto.response.RefundResponse;
import com.swp.autocarwash.refund.entity.enums.BankEnum;
import com.swp.autocarwash.refund.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Controller xử lý luồng hoàn tiền khi Customer hủy booking (US-04).
 *
 * <p>Base URL: {@code /api/refunds}</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;
    private final CustomerRepository customerRepository;

    /**
     * Suy ra customerId của khách đang đăng nhập từ JWT (không nhận từ client — tránh IDOR).
     */
    private Long resolveCustomerId(UserCustomerDetails principal) {
        Customer customer = customerRepository.findByUserId(principal.getUser().getId());
        if (customer == null) {
            throw new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        return customer.getId();
    }

    /**
     * Số tiền cọc cố định toàn hệ thống — FE gọi để hiện trên form AC2 TRƯỚC khi khách
     * bấm "Xác nhận hủy" (không tạo Refund, không đổi status booking).
     */
    @GetMapping("/deposit-amount")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse<DepositAmountResponse>> getDepositAmount() {
        DepositAmountResponse result = refundService.getDepositAmount();
        return ResponseEntity.ok(ApiResponse.success("Lấy số tiền cọc thành công", result));
    }

    /**
     * Danh sách ngân hàng (bin + tên hiển thị) để FE dựng dropdown chọn ngân hàng nhận hoàn tiền (AC2).
     */
    @GetMapping("/banks")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<BankOptionResponse>>> listBanks() {
        List<BankOptionResponse> banks = Arrays.stream(BankEnum.values())
                .map(bank -> BankOptionResponse.builder()
                        .bin(bank.getBin())
                        .displayName(bank.getDisplayName())
                        .shortCode(bank.getShortCode())
                        .build())
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách ngân hàng thành công", banks));
    }

    /**
     * Tra cứu tên chủ tài khoản qua VietQR Lookup (AC2.1).
     */
    @GetMapping("/account-lookup")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse<AccountLookupResponse>> lookupAccount(
            @RequestParam String bin,
            @RequestParam String accountNumber) {
        AccountLookupResponse result = refundService.lookupAccount(bin, accountNumber);
        return ResponseEntity.ok(ApiResponse.success("Tra cứu tài khoản thành công", result));
    }

    /**
     * Customer xác nhận hủy booking và tạo yêu cầu hoàn tiền (AC3).
     */
    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse<RefundResponse>> createRefund(
            @Valid @RequestBody CreateRefundRequest request,
            @AuthenticationPrincipal UserCustomerDetails principal) {
        RefundResponse result = refundService.createRefund(request, resolveCustomerId(principal));
        return ResponseEntity.ok(ApiResponse.success("Đã tạo yêu cầu hoàn tiền, vui lòng chờ hoàn tiền", result));
    }

    /**
     * Danh sách yêu cầu hoàn tiền cho Admin, có filter + search (US-05 AC1, AC6-AC10).
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<RefundListPageResponse>> listRefunds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer stationId,
            @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        RefundListPageResponse result = refundService.listRefundsForAdmin(
                status, year, month, stationId, keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách hoàn tiền thành công", result));
    }

    /**
     * Chi tiết 1 yêu cầu hoàn tiền cho Admin, kèm ảnh QR VietQR nếu chưa xử lý (US-05 AC2, AC2b, AC2c).
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<RefundDetailResponse>> getRefundDetail(@PathVariable Long id) {
        RefundDetailResponse result = refundService.getRefundDetail(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy chi tiết hoàn tiền thành công", result));
    }

    /**
     * Admin xác nhận đã chuyển khoản hoàn tiền xong (US-05 AC3, AC4, AC5).
     */
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<RefundDetailResponse>> confirmRefund(
            @PathVariable Long id,
            @Valid @RequestBody ConfirmRefundRequest request,
            @AuthenticationPrincipal UserCustomerDetails principal) {
        RefundDetailResponse result = refundService.confirmRefund(
                id, request.getTransactionCode(), principal.getUser().getId());
        return ResponseEntity.ok(ApiResponse.success("Xác nhận hoàn tiền thành công", result));
    }
}
