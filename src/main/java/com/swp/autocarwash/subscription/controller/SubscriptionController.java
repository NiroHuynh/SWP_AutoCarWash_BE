package com.swp.autocarwash.subscription.controller;

import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.payment.dto.response.SubscriptionPaymentInitResponse;
import com.swp.autocarwash.payment.service.SubscriptionPaymentService;
import com.swp.autocarwash.subscription.dto.request.RegisterFamilySubscriptionRequest;
import com.swp.autocarwash.subscription.dto.response.ActiveSubscriptionResponse;
import com.swp.autocarwash.subscription.dto.response.FamilySubscriptionPlansResponse;
import com.swp.autocarwash.subscription.dto.response.RegisterFamilySubscriptionResponse;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.repository.FamilySubscriptionRepository;
import com.swp.autocarwash.subscription.service.FamilySubscriptionService;
import com.swp.autocarwash.subscription.service.SubscriptionPlanService;
import com.swp.autocarwash.subscription.service.SubscriptionQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Chức năng: Controller cho khách hàng tự tra cứu thông tin gói subscription
 * của chính mình.
 *
 * <p>Base URL: {@code /api/subscriptions}</p>
 *
 * @author Ngân
 * @version 1.0
 */
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionQueryService subscriptionQueryService;
    private final CustomerRepository customerRepository;
    private final SubscriptionPaymentService subscriptionPaymentService;


    /**
     * Chức năng: Lấy gói subscription đang hoạt động (ACTIVE, còn hạn) của
     * khách hàng đang đăng nhập — tên gói, ngày hết hạn, số ngày còn lại.
     *
     * <p><b>Ví dụ:</b> {@code GET /api/subscriptions/active}</p>
     *
     * @param principal khách hàng đang đăng nhập (suy ra customerId từ JWT)
     * @return {@code 200 OK} với {@link ActiveSubscriptionResponse}, hoặc
     *         {@code 204 No Content} nếu khách không có gói nào đang active
     */
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse<ActiveSubscriptionResponse>> getActiveSubscription(
            @AuthenticationPrincipal UserCustomerDetails principal) {

        Long customerId = resolveCustomerId(principal);
        ActiveSubscriptionResponse result = subscriptionQueryService.getActiveSubscription(customerId);

        if (result == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                ApiResponse.success("Gói subscription đang hoạt động", result)
        );
    }

    /**
     * Chức năng: Tra trạng thái + thông tin thanh toán của một hóa đơn mua gói —
     * FE poll để chuyển UI khi PENDING/PAID/FAILED (FE-US-56-04 AC02/AC03).
     *
     * <p><b>Ví dụ:</b> {@code GET /api/subscriptions/invoices/12}</p>
     */
    @GetMapping("/invoices/{invoiceId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse<SubscriptionPaymentInitResponse>> getInvoiceStatus(
            @PathVariable Long invoiceId,
            @AuthenticationPrincipal UserCustomerDetails principal) {

        Long customerId = resolveCustomerId(principal);
        SubscriptionPaymentInitResponse result =
                subscriptionPaymentService.getInvoiceStatus(invoiceId, customerId);

        return ResponseEntity.ok(
                ApiResponse.success("Trạng thái hóa đơn mua gói", result)
        );
    }

    /** Suy ra customerId của khách đang đăng nhập từ token. */
    private Long resolveCustomerId(UserCustomerDetails principal) {
        Customer customer = customerRepository.findByUserId(principal.getUser().getId());
        if (customer == null) {
            throw new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        return customer.getId();
    }
}
