package com.swp.autocarwash.subscription.listener;

import com.swp.autocarwash.payment.event.SubscriptionInvoicePaidEvent;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;

/**
 * Chức năng: Kích hoạt / gia hạn gói unlimited khi hóa đơn mua gói được thanh
 * toán thành công (FE-US-56-04 AC04).
 *
 * <p>Phân biệt đăng ký mới vs gia hạn bằng chính trạng thái của gói:
 * <ul>
 *   <li>Gói đang PENDING (đăng ký lần đầu) hoặc EXPIRED → kích hoạt mới:
 *       startDate = hôm thanh toán, endDate = +durationDays, status ACTIVE.</li>
 *   <li>Gói đang ACTIVE còn hạn (gia hạn) → cộng dồn nối tiếp: endDate += durationDays.</li>
 * </ul>
 *
 * <p>Dùng {@link TransactionalEventListener} phase AFTER_COMMIT để chắc chắn
 * Invoice/Payment đã commit trước khi cập nhật gói.</p>
 *
 * @author Ngân
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionRenewalListener {

    private final UnlimitSubscriptionRepository unlimitSubscriptionRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSubscriptionInvoicePaid(SubscriptionInvoicePaidEvent event) {
        // Gói FAMILY xử lý riêng, ngoài phạm vi US này
        if (event.getUnlimitSubscriptionId() == null) {
            return;
        }

        UnlimitSubscription sub = unlimitSubscriptionRepository
                .findById(event.getUnlimitSubscriptionId())
                .orElse(null);
        if (sub == null) {
            log.warn("Nhận event thanh toán gói nhưng không tìm thấy UnlimitSubscription {}",
                    event.getUnlimitSubscriptionId());
            return;
        }

        int durationDays = sub.getSubscriptionPlan().getDurationDays();
        LocalDate today = LocalDate.now();

        boolean cumulative = sub.getStatus() == SubscriptionStatus.ACTIVE
                && !sub.getEndDate().isBefore(today);

        if (cumulative) {
            // Gia hạn khi còn hạn: nối tiếp thời hạn hiện có
            sub.setEndDate(sub.getEndDate().plusDays(durationDays));
        } else {
            // Đăng ký mới (PENDING) hoặc gia hạn khi đã hết hạn (EXPIRED): tính từ ngày thanh toán
            sub.setStartDate(today);
            sub.setEndDate(today.plusDays(durationDays));
            sub.setStatus(SubscriptionStatus.ACTIVE);
        }

        unlimitSubscriptionRepository.save(sub);
        log.info("Kích hoạt/gia hạn gói {} thành công, endDate mới = {}",
                sub.getId(), sub.getEndDate());
    }
}
