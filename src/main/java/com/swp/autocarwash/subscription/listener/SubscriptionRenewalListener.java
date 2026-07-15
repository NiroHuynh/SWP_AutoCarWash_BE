package com.swp.autocarwash.subscription.listener;

import com.swp.autocarwash.payment.event.SubscriptionInvoicePaidEvent;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import com.swp.autocarwash.subscription.repository.FamilySubscriptionRepository;
import com.swp.autocarwash.subscription.repository.SubscriptionPlanRepository;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import com.swp.autocarwash.subscription.util.SubscriptionRenewalCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    private final FamilySubscriptionRepository familySubscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onSubscriptionInvoicePaid(SubscriptionInvoicePaidEvent event) {
        if (event.getUnlimitSubscriptionId() != null) {
            activateUnlimitedSubscription(event.getUnlimitSubscriptionId());
        } else if (event.getFamilySubscriptionId() != null) {
            activateFamilySubscription(event.getFamilySubscriptionId(), event.getSubscriptionPlanId());
        }
    }

    private void activateUnlimitedSubscription(Long unlimitSubscriptionId) {
        UnlimitSubscription sub = unlimitSubscriptionRepository
                .findById(unlimitSubscriptionId)
                .orElse(null);
        if (sub == null) {
            log.warn("Nhận event thanh toán gói nhưng không tìm thấy UnlimitSubscription {}",
                    unlimitSubscriptionId);
            return;
        }

        LocalDate today = LocalDate.now();
        boolean cumulative = SubscriptionRenewalCalculator.isCumulative(sub, today);

        sub.setStartDate(SubscriptionRenewalCalculator.calculateEntityStartDate(sub, today));
        sub.setEndDate(SubscriptionRenewalCalculator.calculateEndDate(sub, today));
        if (!cumulative) {
            sub.setStatus(SubscriptionStatus.ACTIVE);
        }

        unlimitSubscriptionRepository.save(sub);
        log.info("Kích hoạt/gia hạn gói {} thành công, endDate mới = {}",
                sub.getId(), sub.getEndDate());
    }

    /**
     * Kích hoạt/gia hạn gói FAMILY khi hóa đơn thanh toán thành công. Bản ghi FamilySubscription
     * được giữ nguyên (status ACTIVE, ngày cũ) suốt lúc chờ thanh toán — mọi thay đổi (đổi gói,
     * tính lại endDate) chỉ áp dụng ở đây, sau khi thanh toán thành công, để nếu payment fail/
     * timeout thì gói hiện tại không bị mất ngày còn lại hay đổi gói ngoài ý muốn.
     *
     * <p>Quy tắc tính ngày giống Unlimited: gói đang ACTIVE và chưa hết hạn (renew sớm) thì cộng
     * dồn nối tiếp từ endDate cũ (dù đổi gói khác); ngược lại (đăng ký lần đầu đang PENDING, hoặc
     * gói đã hết hạn) thì tính lại từ hôm nay.</p>
     */
    private void activateFamilySubscription(Long familySubscriptionId, Integer targetPlanId) {
        FamilySubscription sub = familySubscriptionRepository
                .findById(familySubscriptionId)
                .orElse(null);
        if (sub == null) {
            log.warn("Nhận event thanh toán gói nhưng không tìm thấy FamilySubscription {}",
                    familySubscriptionId);
            return;
        }

        if (targetPlanId != null
                && (sub.getSubscriptionPlan() == null || !targetPlanId.equals(sub.getSubscriptionPlan().getId()))) {
            subscriptionPlanRepository.findById(targetPlanId).ifPresent(sub::setSubscriptionPlan);
        }

        LocalDate today = LocalDate.now();
        boolean cumulative = SubscriptionStatus.ACTIVE.name().equals(sub.getStatus())
                && !sub.getEndDate().isBefore(today);
        int durationDays = sub.getSubscriptionPlan().getDurationDays();

        sub.setStartDate(cumulative ? sub.getStartDate() : today);
        sub.setEndDate(cumulative ? sub.getEndDate().plusDays(durationDays) : today.plusDays(durationDays));
        sub.setStatus(SubscriptionStatus.ACTIVE.name());
        sub.setCanceledAt(null);

        familySubscriptionRepository.save(sub);
        log.info("Kích hoạt/gia hạn gói family {} thành công, endDate mới = {}",
                sub.getId(), sub.getEndDate());
    }
}
