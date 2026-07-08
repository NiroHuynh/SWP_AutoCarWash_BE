package com.swp.autocarwash.payment.service.impl;

import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.payment.dto.request.SubscriptionPaymentInitRequest;
import com.swp.autocarwash.payment.dto.response.SubscriptionPaymentInitResponse;
import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.repository.SubscriptionInvoiceRepository;
import com.swp.autocarwash.payment.service.SubscriptionPaymentService;
import com.swp.autocarwash.payment.util.SePayQrBuilder;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /** Trạng thái hóa đơn subscription: chờ chuyển khoản. */
    public static final String INVOICE_PENDING = "PENDING";
    /** Trạng thái hóa đơn subscription: đã thanh toán. */
    public static final String INVOICE_PAID = "PAID";
    /** Trạng thái hóa đơn subscription: hủy (quá hạn thanh toán). */
    public static final String INVOICE_CANCEL = "CANCEL";

    /** Prefix nội dung chuyển khoản mua gói. */
    public static final String TRANSFER_PREFIX = "SUB";

    private final SubscriptionInvoiceRepository subscriptionInvoiceRepository;
    private final EntityManager entityManager;
    private final SePayQrBuilder sePayQrBuilder;

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

        String transferContent = TRANSFER_PREFIX + invoice.getId();
        return SubscriptionPaymentInitResponse.builder()
                .invoiceId(invoice.getId())
                .transferContent(transferContent)
                .amount(invoice.getPlanPrice())
                .invoiceStatus(invoice.getStatus())
                .qrImageUrl(sePayQrBuilder.buildQrImageUrl(invoice.getPlanPrice(), transferContent))
                .build();
    }
}
