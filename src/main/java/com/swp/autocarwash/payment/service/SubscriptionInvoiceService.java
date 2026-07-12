package com.swp.autocarwash.payment.service;

import com.swp.autocarwash.subscription.dto.request.RenewalPaymentRequest;
import com.swp.autocarwash.subscription.dto.response.RenewalPaymentResponse;

public interface SubscriptionInvoiceService {
    RenewalPaymentResponse completeRenewPayment(
            Long invoiceId,
            RenewalPaymentRequest request
    );
}
