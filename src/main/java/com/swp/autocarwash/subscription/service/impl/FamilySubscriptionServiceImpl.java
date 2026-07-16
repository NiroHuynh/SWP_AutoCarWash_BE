package com.swp.autocarwash.subscription.service.impl;

import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.FamilyGroup;
import com.swp.autocarwash.customer.entity.FamilyMember;
import com.swp.autocarwash.customer.repository.FamilyGroupRepository;
import com.swp.autocarwash.customer.repository.FamilyMemberRepository;
import com.swp.autocarwash.payment.dto.request.SubscriptionPaymentInitRequest;
import com.swp.autocarwash.payment.dto.response.SubscriptionPaymentInitResponse;
import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceStatus;
import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceType;
import com.swp.autocarwash.payment.repository.SubscriptionInvoiceRepository;
import com.swp.autocarwash.payment.service.SubscriptionPaymentService;
import com.swp.autocarwash.subscription.dto.request.RegisterFamilySubscriptionRequest;
import com.swp.autocarwash.subscription.dto.request.RenewFamilySubscriptionRequest;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.entity.SubscriptionPlan;
import com.swp.autocarwash.subscription.entity.enums.PlanType;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionPlanStatus;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import com.swp.autocarwash.subscription.repository.FamilySubscriptionRepository;
import com.swp.autocarwash.subscription.repository.SubscriptionPlanRepository;
import com.swp.autocarwash.subscription.service.FamilySubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class FamilySubscriptionServiceImpl implements FamilySubscriptionService {


    private final FamilySubscriptionRepository familySubscriptionRepository;
    private final FamilyGroupRepository familyGroupRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionInvoiceRepository subscriptionInvoiceRepository;
    private final SubscriptionPaymentService subscriptionPaymentService;
    private final SecurityUtils securityUtils;

    @Override
    public Integer getActiveServicePackageId(Long vehicleId) {

        if (vehicleId == null) {
            return null;
        }
        Integer servicePackageId =  familySubscriptionRepository
                .findActiveServicePackageIdByVehicleId(vehicleId);

        return servicePackageId;
    }

    @Override
    public boolean hasFamilySubscription(Long vehicle, Integer servicePackageId) {
        return familySubscriptionRepository.existsActiveFamilySubscription(vehicle,servicePackageId);
    }


    @Override
    @Transactional
    public SubscriptionPaymentInitResponse registerFamilySubscription(
            RegisterFamilySubscriptionRequest request) {

        // ===== Get current customer =====
        Customer customer = securityUtils.getCustomer();

        if(customer==null) throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);

        // ===== Validate Family Group =====
        FamilyGroup familyGroup = familyGroupRepository
                .findByIdAndIsDeletedFalse(request.getFamilyGroupId())
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.FAMILY_GROUP_NOT_FOUND));

        // ===== Validate Owner =====
        if (!familyGroup.getOwnerCustomer().getId().equals(customer.getId())) {
            throw new BusinessException(ErrorCode.FAMILY_GROUP_NOT_OWNED);
        }

        // ===== Validate Subscription Plan =====
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository
                .findByIdAndIsDeletedFalse(request.getSubscriptionPlanId())
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.INVALID_SUBSCRIPTION_PLAN));

        if (!PlanType.FAMILY.name().equals(subscriptionPlan.getPlanType())
                || subscriptionPlan.getStatus() != SubscriptionPlanStatus.ACTIVE) {

            throw new BusinessException(ErrorCode.INVALID_SUBSCRIPTION_PLAN);
        }

        // ===== Check active subscription =====
        boolean hasActiveSubscription = familySubscriptionRepository
                .existsByFamilyGroupAndStatus(
                        familyGroup,
                        SubscriptionStatus.ACTIVE.name()
                );

        if (hasActiveSubscription) {
            throw new BusinessException(
                    ErrorCode.FAMILY_SUBSCRIPTION_ALREADY_ACTIVE
            );
        }

        // ===== Idempotent: nhóm đã có subscription PENDING kèm invoice PENDING -> trả lại QR đó =====
        FamilySubscription latest = familySubscriptionRepository
                .findFirstByFamilyGroupOrderByIdDesc(familyGroup)
                .orElse(null);
        if (latest != null && SubscriptionStatus.PENDING.name().equals(latest.getStatus())) {
            SubscriptionInvoice existing = subscriptionInvoiceRepository
                    .findFirstByFamilySubscription_IdAndStatusOrderByCreatedAtDesc(
                            latest.getId(), SubscriptionInvoiceStatus.PENDING.name())
                    .orElse(null);
            if (existing != null) {
                return subscriptionPaymentService.getInvoiceStatus(existing.getId(), customer.getId());
            }
        }

        // ===== Create Family Subscription (PENDING, chưa hưởng quyền lợi tới khi thanh toán) =====
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(
                subscriptionPlan.getDurationDays()
        );

        FamilySubscription familySubscription = new FamilySubscription();

        familySubscription.setFamilyGroup(familyGroup);
        familySubscription.setSubscriptionPlan(subscriptionPlan);
        familySubscription.setStartDate(startDate);
        familySubscription.setEndDate(endDate);
        familySubscription.setStatus(
                SubscriptionStatus.PENDING.name()
        );

        familySubscription = familySubscriptionRepository.save(familySubscription);

        return subscriptionPaymentService.initiatePayment(
                SubscriptionPaymentInitRequest.builder()
                        .customerId(customer.getId())
                        .planPrice(subscriptionPlan.getPrice())
                        .planName(subscriptionPlan.getPlanName())
                        .familySubscriptionId(familySubscription.getId())
                        .type(SubscriptionInvoiceType.REGISTER)
                        .build());
    }


    @Override
    @Transactional
    public void cancelFamilySubscription() {

        Customer customer = securityUtils.getCustomer();

        // Tìm FamilyGroup thông qua FamilyMember
        FamilyMember familyMember = familyMemberRepository
                .findByCustomer(customer)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.FAMILY_GROUP_NOT_FOUND));

        FamilyGroup familyGroup = familyMember.getFamilyGroup();

        // Kiểm tra Owner
        if (!familyGroup.getOwnerCustomer().getId().equals(customer.getId())) {
            throw new BusinessException(ErrorCode.FAMILY_GROUP_NOT_OWNED);
        }

        // Lấy subscription mới nhất
        FamilySubscription familySubscription = familySubscriptionRepository
                .findFirstByFamilyGroupOrderByIdDesc(familyGroup)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.FAMILY_SUBSCRIPTION_NOT_FOUND));

        // Kiểm tra ACTIVE
        if (!SubscriptionStatus.ACTIVE.name()
                .equals(familySubscription.getStatus())) {

            throw new BusinessException(
                    ErrorCode.FAMILY_SUBSCRIPTION_NOT_ACTIVE
            );
        }

        // Cancel subscription
        familySubscription.setStatus(
                SubscriptionStatus.CANCELED.name()
        );
        familySubscription.setCanceledAt(LocalDateTime.now());

        familySubscriptionRepository.save(familySubscription);
    }

    @Override
    @Transactional
    public SubscriptionPaymentInitResponse renewFamilySubscription(
            RenewFamilySubscriptionRequest request) {

        Customer customer = securityUtils.getCustomer();

        FamilyGroup familyGroup = getOwnerFamilyGroup(customer);

        SubscriptionPlan subscriptionPlan = validateSubscriptionPlan(
                request.getSubscriptionPlanId());

        FamilySubscription currentSubscription =
                getLatestFamilySubscription(familyGroup);

        SubscriptionPaymentInitResponse checkPendingInvoice = checkPendingInvoice(currentSubscription, customer);

        if(checkPendingInvoice!=null) return checkPendingInvoice;

        if (isSamePlan(currentSubscription, subscriptionPlan)) {
            return renewSamePlan(
                    customer,
                    currentSubscription,
                    subscriptionPlan
            );
        }

        return switchSubscriptionPlan(
                customer,
                familyGroup,
                currentSubscription,
                subscriptionPlan
        );
    }

    private FamilyGroup getOwnerFamilyGroup(Customer customer) {

        FamilyMember familyMember = familyMemberRepository
                .findByCustomer(customer)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.FAMILY_GROUP_NOT_FOUND));

        FamilyGroup familyGroup = familyMember.getFamilyGroup();

        if (!familyGroup.getOwnerCustomer().getId().equals(customer.getId())) {
            throw new BusinessException(ErrorCode.FAMILY_GROUP_NOT_OWNED);
        }

        return familyGroup;
    }

    private SubscriptionPlan validateSubscriptionPlan(Integer planId) {

        SubscriptionPlan subscriptionPlan =
                subscriptionPlanRepository
                        .findByIdAndIsDeletedFalse(planId)
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.INVALID_SUBSCRIPTION_PLAN));

        if (!PlanType.FAMILY.name().equals(subscriptionPlan.getPlanType())
                || subscriptionPlan.getStatus() != SubscriptionPlanStatus.ACTIVE) {

            throw new BusinessException(
                    ErrorCode.INVALID_SUBSCRIPTION_PLAN);
        }

        return subscriptionPlan;
    }


    private FamilySubscription getLatestFamilySubscription(
            FamilyGroup familyGroup) {

        return familySubscriptionRepository
                .findFirstByFamilyGroupOrderByIdDesc(familyGroup)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.FAMILY_SUBSCRIPTION_NOT_FOUND));
    }

    private SubscriptionPaymentInitResponse checkPendingInvoice(
            FamilySubscription familySubscription,
            Customer customer) {
        // ===== Idempotent: đã có hóa đơn PENDING đang chờ trên bản ghi mới nhất -> trả lại QR đó =====
        SubscriptionInvoice existing = subscriptionInvoiceRepository
                .findFirstByFamilySubscription_IdAndStatusOrderByCreatedAtDesc(
                        familySubscription.getId(), SubscriptionInvoiceStatus.PENDING.name())
                .orElse(null);

        if (existing != null) {
            return subscriptionPaymentService.getInvoiceStatus(existing.getId(), customer.getId());
        }
        return null;
    }

    private boolean isSamePlan(
            FamilySubscription subscription,
            SubscriptionPlan plan) {

        return subscription.getSubscriptionPlan()
                .getId()
                .equals(plan.getId());
    }

    private SubscriptionPaymentInitResponse renewSamePlan(
            Customer customer,
            FamilySubscription subscription,
            SubscriptionPlan subscriptionPlan) {

        // KHÔNG đụng status/endDate ở đây — gói hiện tại phải giữ nguyên ACTIVE trong lúc chờ
        // thanh toán, và nếu thanh toán fail/timeout thì không mất ngày còn lại. endDate chỉ
        // được cộng dồn từ ngày hết hạn cũ sau khi thanh toán thành công, xem
        // SubscriptionRenewalListener.activateFamilySubscription.
        return subscriptionPaymentService.initiatePayment(
                SubscriptionPaymentInitRequest.builder()
                        .customerId(customer.getId())
                        .familySubscriptionId(subscription.getId())
                        .subscriptionPlanId(subscriptionPlan.getId())
                        .planPrice(subscriptionPlan.getPrice())
                        .planName(subscriptionPlan.getPlanName())
                        .type(SubscriptionInvoiceType.RENEW)
                        .build());
    }

    private SubscriptionPaymentInitResponse switchSubscriptionPlan(
            Customer customer,
            FamilyGroup familyGroup,
            FamilySubscription currentSubscription,
            SubscriptionPlan newPlan) {

        // Chưa cancel gói cũ / tạo bản ghi mới ở đây — chỉ initiate payment với subscription hiện tại.
        // Việc tạo bản ghi FamilySubscription mới (ACTIVE) + cancel bản ghi cũ chỉ thực hiện SAU KHI
        // thanh toán thành công, xem SubscriptionRenewalListener.activateFamilyPlanSwitch.
        return subscriptionPaymentService.initiatePayment(
                SubscriptionPaymentInitRequest.builder()
                        .customerId(customer.getId())
                        .familySubscriptionId(currentSubscription.getId())
                        .subscriptionPlanId(newPlan.getId())
                        .planPrice(newPlan.getPrice())
                        .planName(newPlan.getPlanName())
                        .type(SubscriptionInvoiceType.RENEW)
                        .build());
    }
}
