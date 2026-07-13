package com.swp.autocarwash.payment.service.impl;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.entity.enums.PaymentStatus;
import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceStatus;
import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceType;
import com.swp.autocarwash.payment.repository.SubscriptionInvoiceRepository;
import com.swp.autocarwash.payment.service.SubscriptionInvoiceService;
import com.swp.autocarwash.subscription.dto.request.RenewalPaymentRequest;
import com.swp.autocarwash.subscription.dto.response.RenewalPaymentResponse;
import com.swp.autocarwash.subscription.dto.response.UnlimitedSubscriptionResponse;
import com.swp.autocarwash.subscription.entity.SubscriptionPlan;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionInvoiceServiceImpl implements SubscriptionInvoiceService {

    private final SubscriptionInvoiceRepository subscriptionInvoiceRepository;
    private final UnlimitSubscriptionRepository unlimitSubscriptionRepository;

    @Override
    public RenewalPaymentResponse completeRenewPayment(
            Long invoiceId,
            RenewalPaymentRequest request) {

        try{
            PaymentStatus.valueOf(request.getPaymentStatus());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_PAYMENT_STATUS);
        }

        SubscriptionInvoice invoice =
                subscriptionInvoiceRepository
                        .findById(invoiceId)
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.SUBSCRIPTION_INVOICE_NOT_FOUND));

        if (invoice.getType()
                != SubscriptionInvoiceType.RENEW) {

            throw new BusinessException(
                    ErrorCode.INVALID_RENEWAL_INVOICE);
        }

        if (!invoice.getStatus().equals(SubscriptionInvoiceStatus.PENDING.name())) {
            throw new BusinessException(
                    ErrorCode.INVOICE_ALREADY_PROCESSED);
        }

        // Endpoint này chỉ xử lý gia hạn gói Unlimited; hóa đơn gói FAMILY phải xác nhận qua
        // webhook SePay hoặc SubscriptionInvoiceService.manualConfirmSubscription (đã generic).
        if (invoice.getFamilySubscription() != null) {
            throw new BusinessException(
                    ErrorCode.INVALID_RENEWAL_INVOICE);
        }

        UnlimitSubscription subscription =
                invoice.getUnlimitSubscription();

        if (PaymentStatus.valueOf(request.getPaymentStatus()) == PaymentStatus.SUCCESS) {

            invoice.setStatus(SubscriptionInvoiceStatus.PAID.name());
            invoice.setPaidAt(LocalDateTime.now());

            SubscriptionPlan plan =
                    subscription.getSubscriptionPlan();

            subscription.setEndDate(
                    subscription.getEndDate()
                            .plusDays(plan.getDurationDays())
            );

            subscription.setStatus(SubscriptionStatus.ACTIVE);

            unlimitSubscriptionRepository.save(subscription);

        } else {

            invoice.setStatus(SubscriptionInvoiceStatus.FAILED.name());
        }

        subscriptionInvoiceRepository.save(invoice);

        return RenewalPaymentResponse.builder()
                .subscriptionId(subscription.getId())
                .invoiceId(invoice.getId())
                .status(subscription.getStatus())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .build();
    }
}
