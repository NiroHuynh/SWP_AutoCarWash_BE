package com.swp.autocarwash.payment.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.payment.entity.enums.PaymentStatus;
import com.swp.autocarwash.payment.service.SubscriptionInvoiceService;
import com.swp.autocarwash.subscription.dto.request.RenewalPaymentRequest;
import com.swp.autocarwash.subscription.dto.response.RenewalPaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubscriptionInvoiceController {

    private final SubscriptionInvoiceService subscriptionInvoiceService;

    @PatchMapping("/api/customer/subscription-invoices/{invoiceId}/renewal/payment")
    public ApiResponse<RenewalPaymentResponse>
    completeRenewPayment(
            @PathVariable Long invoiceId,
            @Valid @RequestBody RenewalPaymentRequest request) {

        RenewalPaymentResponse response =
                subscriptionInvoiceService.completeRenewPayment(
                        invoiceId,
                        request
                );

        String message =
                PaymentStatus.valueOf(request.getPaymentStatus()) == PaymentStatus.SUCCESS
                        ? "Subscription renewed successfully."
                        : "Subscription renewal failed.";

        return ApiResponse.success(
                        message,
                        response
                );
    }
}
