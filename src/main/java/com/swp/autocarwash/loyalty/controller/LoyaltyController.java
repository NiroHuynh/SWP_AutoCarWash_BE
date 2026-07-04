package com.swp.autocarwash.loyalty.controller;

import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyHistoryResponse;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyProfileResponse;
import com.swp.autocarwash.loyalty.dto.response.TierHistoryResponse;
import com.swp.autocarwash.loyalty.dto.response.TierResponse;
import com.swp.autocarwash.loyalty.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller cho tinh nang Loyalty cua khach hang (FE-42-US-01):
 * xem diem, ngay reset thuong nien, chi tieu va lich su diem.
 *
 * <p>Base URL: {@code /api/loyalty}. Chi khach hang (authority CUSTOMER) truy cap.
 * customerId luon suy ra tu JWT (chong IDOR), khong nhan tu client.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@RestController
@RequestMapping("/api/loyalty")
@RequiredArgsConstructor
public class LoyaltyController {

    private final LoyaltyService loyaltyService;
    private final CustomerRepository customerRepository;

    /** Suy ra customerId cua khach dang dang nhap tu token. */
    private Long resolveCustomerId(UserCustomerDetails principal) {
        Customer customer = customerRepository.findByUserId(principal.getUser().getId());
        if (customer == null) {
            throw new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        return customer.getId();
    }

    /**
     * Trang Loyalty Profile: so du diem, ngay reset + countdown, tien do len hang,
     * tong chi tieu nam hien tai va tien do giu hang (AC01).
     */
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse<LoyaltyProfileResponse>> getProfile(
            @AuthenticationPrincipal UserCustomerDetails principal) {
        Long customerId = resolveCustomerId(principal);
        LoyaltyProfileResponse data = loyaltyService.getLoyaltyProfile(customerId);
        return ResponseEntity.ok(ApiResponse.success("Lay thong tin loyalty thanh cong", data));
    }

    /**
     * Points History: tong chi tieu ca nam + danh sach giao dich diem theo filter nam/thang (AC02).
     *
     * @param year  nam can xem (tuy chon, mac dinh nam hien tai)
     * @param month thang can loc 1-12 (tuy chon, khong truyen = ca nam)
     */
    @GetMapping("/history")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse<LoyaltyHistoryResponse>> getHistory(
            @AuthenticationPrincipal UserCustomerDetails principal,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        Long customerId = resolveCustomerId(principal);
        LoyaltyHistoryResponse data = loyaltyService.getPointHistory(customerId, year, month);
        return ResponseEntity.ok(ApiResponse.success("Lay lich su diem thanh cong", data));
    }

    /**
     * Get Tier List: tra cuu tat ca hang thanh vien kem nguong diem va quyen loi, sap theo minPoints tang dan (AC03).
     */
    @GetMapping("/tiers")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<TierResponse>>> getTierList() {
        List<TierResponse> data = loyaltyService.getTierList();
        return ResponseEntity.ok(ApiResponse.success("Lay danh sach hang thanh vien thanh cong", data));
    }

    /**
     * Tier History: lich su chuyen hang thanh vien cua khach dang dang nhap, filter theo nam/thang.
     *
     * @param year  nam can xem (tuy chon, mac dinh nam hien tai)
     * @param month thang can loc 1-12 (tuy chon, khong truyen = ca nam)
     */
    @GetMapping("/tier-history")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<TierHistoryResponse>>> getTierHistory(
            @AuthenticationPrincipal UserCustomerDetails principal,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        Long customerId = resolveCustomerId(principal);
        List<TierHistoryResponse> data = loyaltyService.getTierHistory(customerId, year, month);
        return ResponseEntity.ok(ApiResponse.success("Lay lich su hang thanh vien thanh cong", data));
    }
}
