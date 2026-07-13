package com.swp.autocarwash.subscription.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.payment.dto.response.SubscriptionPaymentInitResponse;
import com.swp.autocarwash.subscription.dto.request.RegisterUnlimitedSubscriptionRequest;
import com.swp.autocarwash.subscription.service.SubscriptionPurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Chức năng: Mua/gia hạn gói Unlimited CÓ thanh toán QR (FE-US-56-04).
 *
 * <p>Controller riêng (không đụng {@code UnlimitedSubscriptionController} của
 * Phong). Endpoint đăng ký cũ {@code POST /api/customer/unlimited-subscriptions}
 * của Phong giữ nguyên; FE dùng các endpoint tại đây cho luồng có thanh toán.</p>
 *
 * <p>Base URL: {@code /api/customer/subscriptions/unlimited}</p>
 *
 * @author Ngân
 * @version 1.0
 */
@RestController
@RequestMapping("/api/customer/subscriptions/unlimited")
@RequiredArgsConstructor
public class SubscriptionPurchaseController {

    private final SubscriptionPurchaseService subscriptionPurchaseService;

    /**
     * Đăng ký gói Unlimited mới — tạo gói PENDING + hóa đơn QR để khách thanh toán (AC01).
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<SubscriptionPaymentInitResponse> register(
            @Valid @RequestBody RegisterUnlimitedSubscriptionRequest request) {

        return ApiResponse.success(
                "Khởi tạo thanh toán đăng ký gói thành công.",
                subscriptionPurchaseService.registerUnlimited(request)
        );
    }

    /**
     * Gia hạn gói Unlimited đang có — tạo hóa đơn gia hạn QR (FE-US-56-04).
     */
    @PostMapping("/{id}/renew")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<SubscriptionPaymentInitResponse> renew(@PathVariable Long id) {

        return ApiResponse.success(
                "Khởi tạo thanh toán gia hạn gói thành công.",
                subscriptionPurchaseService.renewUnlimited(id)
        );
    }
}
