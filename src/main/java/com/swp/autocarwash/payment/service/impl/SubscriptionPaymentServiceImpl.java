package com.swp.autocarwash.payment.service.impl;

import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.payment.dto.request.SubscriptionPaymentInitRequest;
import com.swp.autocarwash.payment.dto.response.SubscriptionPaymentInitResponse;
import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.config.SePayBankProperties;
import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceStatus;
import com.swp.autocarwash.payment.repository.SubscriptionInvoiceRepository;
import com.swp.autocarwash.payment.service.SubscriptionPaymentService;
import com.swp.autocarwash.payment.util.SePayQrBuilder;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.util.SubscriptionRenewalCalculator;
import com.swp.autocarwash.system.service.SystemSettingService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Triển khai khởi tạo thanh toán mua gói định nghĩa trong
 * {@link SubscriptionPaymentService}.
 *
 * @author Ngân
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class SubscriptionPaymentServiceImpl implements SubscriptionPaymentService {

    /** Trạng thái hóa đơn subscription — nguồn hoá từ enum {@link SubscriptionInvoiceStatus}. */
    public static final String INVOICE_PENDING = SubscriptionInvoiceStatus.PENDING.name();
    /** Trạng thái hóa đơn subscription: đã thanh toán. */
    public static final String INVOICE_PAID = SubscriptionInvoiceStatus.PAID.name();
    /** Trạng thái hóa đơn subscription: thất bại (quá hạn thanh toán) — AC03. */
    public static final String INVOICE_FAILED = SubscriptionInvoiceStatus.FAILED.name();

    /** Prefix nội dung chuyển khoản mua gói. */
    public static final String TRANSFER_PREFIX = "SUB";

    private final SubscriptionInvoiceRepository subscriptionInvoiceRepository;
    private final EntityManager entityManager;
    private final SePayQrBuilder sePayQrBuilder;
    private final SystemSettingService systemSettingService;
    private final SePayBankProperties sePayBankProperties;

    /** {@inheritDoc} */
    @Override
    @Transactional
    public SubscriptionPaymentInitResponse initiatePayment(SubscriptionPaymentInitRequest request) {
        SubscriptionInvoice invoice = new SubscriptionInvoice();
        invoice.setCustomer(entityManager.getReference(Customer.class, request.getCustomerId()));
        if (request.getUnlimitSubscriptionId() != null) {
            invoice.setUnlimitSubscription(
                    entityManager.getReference(UnlimitSubscription.class, request.getUnlimitSubscriptionId()));
        }
        if (request.getFamilySubscriptionId() != null) {
            invoice.setFamilySubscription(
                    entityManager.getReference(FamilySubscription.class, request.getFamilySubscriptionId()));
        }
        invoice.setPlanPrice(request.getPlanPrice());
        invoice.setStatus(INVOICE_PENDING);

        invoice = subscriptionInvoiceRepository.save(invoice);

        return buildResponse(invoice, request.getPlanName());
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public SubscriptionPaymentInitResponse getInvoiceStatus(Long invoiceId, Long customerId) {
        SubscriptionInvoice invoice = subscriptionInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.SUBSCRIPTION_INVOICE_NOT_FOUND));

        if (invoice.getCustomer() == null || !invoice.getCustomer().getId().equals(customerId)) {
            throw new ResourceNotFoundException(ErrorCode.SUBSCRIPTION_INVOICE_NOT_FOUND);
        }

        String planName = null;
        if (invoice.getUnlimitSubscription() != null
                && invoice.getUnlimitSubscription().getSubscriptionPlan() != null) {
            planName = invoice.getUnlimitSubscription().getSubscriptionPlan().getPlanName();
        }
        return buildResponse(invoice, planName);
    }

    /**
     * Dựng response thanh toán từ 1 hóa đơn: transferContent "SUB{id}", expiresAt =
     * createdAt + timeout, và qrImageUrl chỉ set khi hóa đơn còn PENDING (hết hạn/đã trả
     * thì không hiển thị QR nữa).
     */
    private SubscriptionPaymentInitResponse buildResponse(SubscriptionInvoice invoice, String planName) {
        String transferContent = TRANSFER_PREFIX + invoice.getId();
        int timeoutMinutes = systemSettingService.getPendingPaymentTimeoutMinutes();
        boolean pending = INVOICE_PENDING.equals(invoice.getStatus());

        UnlimitSubscription unlimitSubscription = invoice.getUnlimitSubscription();

        return SubscriptionPaymentInitResponse.builder()
                .invoiceId(invoice.getId())
                .planName(planName)
                .durationDays(unlimitSubscription != null
                        ? unlimitSubscription.getSubscriptionPlan().getDurationDays() : null)
                .transferContent(transferContent)
                .amount(invoice.getPlanPrice())
                .invoiceStatus(invoice.getStatus())
                .expiresAt(invoice.getCreatedAt() != null
                        ? invoice.getCreatedAt().plus(timeoutMinutes, ChronoUnit.MINUTES)
                        : null)
                .qrImageUrl(pending
                        ? sePayQrBuilder.buildQrImageUrl(invoice.getPlanPrice(), transferContent)
                        : null)
                .bankAccountNumber(sePayBankProperties.getAccountNumber())
                .bankCode(sePayBankProperties.getBankCode())
                .bankAccountName(sePayBankProperties.getAccountName())
                .customerName(invoice.getCustomer() != null ? invoice.getCustomer().getFullName() : null)
                .vehicleLicensePlate(unlimitSubscription != null
                        ? unlimitSubscription.getVehicle().getLicensePlate() : null)
                .startDate(unlimitSubscription != null
                        ? SubscriptionRenewalCalculator.calculateDisplayPeriodStart(unlimitSubscription, LocalDate.now())
                        : null)
                .endDate(unlimitSubscription != null
                        ? SubscriptionRenewalCalculator.calculateEndDate(unlimitSubscription, LocalDate.now())
                        : null)
                .build();
    }
}
