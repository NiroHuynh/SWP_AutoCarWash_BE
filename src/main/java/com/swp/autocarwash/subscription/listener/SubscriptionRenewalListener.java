package com.swp.autocarwash.subscription.listener;

import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.event.SubscriptionInvoicePaidEvent;
import com.swp.autocarwash.payment.repository.SubscriptionInvoiceRepository;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.entity.SubscriptionPlan;
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
import java.time.LocalDateTime;

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
    private final SubscriptionInvoiceRepository subscriptionInvoiceRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onSubscriptionInvoicePaid(SubscriptionInvoicePaidEvent event) {
        if (event.getUnlimitSubscriptionId() != null) {
            activateUnlimitedSubscription(event.getUnlimitSubscriptionId());
        } else if (event.getFamilySubscriptionId() != null) {
            activateFamilySubscription(event.getFamilySubscriptionId(), event.getSubscriptionPlanId(), event.getInvoiceId());
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
    private void activateFamilySubscription(Long familySubscriptionId, Integer targetPlanId, Long invoiceId) {
        FamilySubscription sub = familySubscriptionRepository
                .findById(familySubscriptionId)
                .orElse(null);
        if (sub == null) {
            log.warn("Nhận event thanh toán gói nhưng không tìm thấy FamilySubscription {}",
                    familySubscriptionId);
            return;
        }

        // Đổi gói thật sự (renew khác gói lúc đang ACTIVE) -> tạo bản ghi mới ACTIVE + cancel bản ghi cũ,
        // KHÔNG update tại chỗ. Renew cùng gói và kích hoạt lần đầu (PENDING -> ACTIVE) vẫn update tại chỗ
        // như cũ, vì registerFamilySubscription không set subscriptionPlanId trên request (targetPlanId
        // null) và một bản ghi PENDING không thoả điều kiện status ACTIVE bên dưới.
        boolean isPlanSwitch = targetPlanId != null
                && SubscriptionStatus.ACTIVE.name().equals(sub.getStatus())
                && sub.getSubscriptionPlan() != null
                && !targetPlanId.equals(sub.getSubscriptionPlan().getId());

        if (isPlanSwitch) {
            activateFamilyPlanSwitch(sub, targetPlanId, invoiceId);
            return;
        }

        if (targetPlanId != null && sub.getSubscriptionPlan() == null) {
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

    /**
     * Đổi gói Family: KHÔNG update bản ghi hiện tại — tạo bản ghi FamilySubscription mới (ACTIVE, gói mới,
     * ngày tính theo đúng công thức cộng dồn hiện có nếu gói cũ chưa hết hạn), đồng thời chuyển bản ghi cũ
     * sang CANCELED. Không đụng tới bảng payment.
     */
    private void activateFamilyPlanSwitch(FamilySubscription oldSub, Integer targetPlanId, Long invoiceId) {
        SubscriptionPlan newPlan = subscriptionPlanRepository.findById(targetPlanId).orElse(null);
        if (newPlan == null) {
            log.warn("Không tìm thấy gói đích {} khi đổi gói cho FamilySubscription {}",
                    targetPlanId, oldSub.getId());
            return;
        }

        LocalDate today = LocalDate.now();
        boolean cumulative = !oldSub.getEndDate().isBefore(today);
        LocalDate newStart = cumulative ? oldSub.getStartDate() : today;
        LocalDate newEnd = cumulative
                ? oldSub.getEndDate().plusDays(newPlan.getDurationDays())
                : today.plusDays(newPlan.getDurationDays());

        FamilySubscription newSub = new FamilySubscription();
        newSub.setFamilyGroup(oldSub.getFamilyGroup());
        newSub.setSubscriptionPlan(newPlan);
        newSub.setStartDate(newStart);
        newSub.setEndDate(newEnd);
        newSub.setStatus(SubscriptionStatus.ACTIVE.name());
        newSub = familySubscriptionRepository.save(newSub);

        oldSub.setStatus(SubscriptionStatus.CANCELED.name());
        oldSub.setCanceledAt(LocalDateTime.now());
        familySubscriptionRepository.save(oldSub);

        if (invoiceId != null) {
            Long newSubId = newSub.getId();
            subscriptionInvoiceRepository.findById(invoiceId).ifPresent(invoice -> {
                invoice.setFamilySubscription(
                        familySubscriptionRepository.getReferenceById(newSubId));
                subscriptionInvoiceRepository.save(invoice);
            });
        }

        log.info("Đổi gói family: subscription cũ {} -> CANCELED, subscription mới {} ({}) ACTIVE, endDate = {}",
                oldSub.getId(), newSub.getId(), newPlan.getPlanName(), newSub.getEndDate());
    }
}
