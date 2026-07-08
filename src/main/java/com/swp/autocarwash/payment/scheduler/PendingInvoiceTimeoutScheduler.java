package com.swp.autocarwash.payment.scheduler;

import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.repository.SubscriptionInvoiceRepository;
import com.swp.autocarwash.payment.service.impl.SubscriptionPaymentServiceImpl;
import com.swp.autocarwash.system.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Chức năng: Job tự động hủy hóa đơn mua gói (SubscriptionInvoice) đang PENDING
 * quá hạn chuyển khoản (dùng chung setting PENDING_PAYMENT_TIMEOUT_MINUTES với
 * booking). Không có slot/voucher phải trả — chỉ set status CANCEL; khách chuyển
 * khoản trễ sẽ được webhook log INVALID_INVOICE_STATE cho admin đối soát.
 *
 * @author Ngân
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PendingInvoiceTimeoutScheduler {

    private final SubscriptionInvoiceRepository subscriptionInvoiceRepository;
    private final SystemSettingService systemSettingService;

    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void cancelExpiredPendingInvoices() {
        try {
            int timeoutMinutes = systemSettingService.getPendingPaymentTimeoutMinutes();
            Instant cutoff = Instant.now().minus(timeoutMinutes, ChronoUnit.MINUTES);

            List<SubscriptionInvoice> expiredInvoices = subscriptionInvoiceRepository
                    .findByStatusAndCreatedAtBefore(SubscriptionPaymentServiceImpl.INVOICE_PENDING, cutoff);

            for (SubscriptionInvoice invoice : expiredInvoices) {
                invoice.setStatus(SubscriptionPaymentServiceImpl.INVOICE_CANCEL);
                subscriptionInvoiceRepository.save(invoice);
                log.info("Hủy hóa đơn mua gói {} vì quá {} phút chưa chuyển khoản",
                        invoice.getId(), timeoutMinutes);
            }
        } catch (Exception e) {
            log.error("PendingInvoiceTimeoutScheduler lỗi: {}", e.getMessage(), e);
        }
    }
}
