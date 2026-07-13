package com.swp.autocarwash.payment.scheduler;

import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.repository.SubscriptionInvoiceRepository;
import com.swp.autocarwash.payment.service.impl.SubscriptionPaymentServiceImpl;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import com.swp.autocarwash.system.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Chức năng: Job tự động cho hóa đơn mua gói (SubscriptionInvoice) đang PENDING
 * quá hạn chuyển khoản chuyển sang FAILED (AC03), đồng thời hủy gói đăng ký lần
 * đầu còn đang PENDING gắn với hóa đơn đó (gói chưa thanh toán -> CANCELED).
 * Dùng chung setting PENDING_PAYMENT_TIMEOUT_MINUTES với booking.
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
            LocalDateTime cutoff = LocalDateTime.now().minus(timeoutMinutes, ChronoUnit.MINUTES);

            List<SubscriptionInvoice> expiredInvoices = subscriptionInvoiceRepository
                    .findByStatusAndCreatedAtBefore(SubscriptionPaymentServiceImpl.INVOICE_PENDING, cutoff);

            for (SubscriptionInvoice invoice : expiredInvoices) {
                invoice.setStatus(SubscriptionPaymentServiceImpl.INVOICE_FAILED);
                subscriptionInvoiceRepository.save(invoice);

                // Gói đăng ký lần đầu còn PENDING (chưa kích hoạt) -> hủy theo hóa đơn quá hạn.
                // Gói gia hạn (ACTIVE) không bị đụng vì chỉ nhắm status PENDING.
                UnlimitSubscription sub = invoice.getUnlimitSubscription();
                if (sub != null && sub.getStatus() == SubscriptionStatus.PENDING) {
                    sub.setStatus(SubscriptionStatus.CANCELED);
                }

                FamilySubscription familySub = invoice.getFamilySubscription();
                if (familySub != null && SubscriptionStatus.PENDING.name().equals(familySub.getStatus())) {
                    familySub.setStatus(SubscriptionStatus.CANCELED.name());
                }

                log.info("Hóa đơn mua gói {} quá {} phút chưa chuyển khoản -> FAILED",
                        invoice.getId(), timeoutMinutes);
            }
        } catch (Exception e) {
            log.error("PendingInvoiceTimeoutScheduler lỗi: {}", e.getMessage(), e);
        }
    }
}
