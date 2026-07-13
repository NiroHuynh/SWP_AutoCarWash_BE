package com.swp.autocarwash.subscription.service.impl;

import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.FamilyGroup;
import com.swp.autocarwash.customer.entity.FamilyMember;
import com.swp.autocarwash.customer.repository.FamilyGroupRepository;
import com.swp.autocarwash.customer.repository.FamilyMemberRepository;
import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceStatus;
import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceType;
import com.swp.autocarwash.payment.repository.SubscriptionInvoiceRepository;
import com.swp.autocarwash.subscription.dto.request.RegisterFamilySubscriptionRequest;
import com.swp.autocarwash.subscription.dto.request.RenewFamilySubscriptionRequest;
import com.swp.autocarwash.subscription.dto.response.RegisterFamilySubscriptionResponse;
import com.swp.autocarwash.subscription.dto.response.RenewFamilySubscriptionResponse;
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
    public RegisterFamilySubscriptionResponse registerFamilySubscription(
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

        // ===== Create Family Subscription =====
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

        // ===== Create Invoice =====
        SubscriptionInvoice invoice = SubscriptionInvoice.builder()
                .customer(customer)
                .familySubscription(familySubscription)
                .planPrice(subscriptionPlan.getPrice())
                .status(SubscriptionInvoiceStatus.PENDING.name())
                .type(SubscriptionInvoiceType.REGISTER)
                .build();

        invoice = subscriptionInvoiceRepository.save(invoice);

        // ===== Response =====
        return RegisterFamilySubscriptionResponse.builder()
                .familySubscriptionId(familySubscription.getId())
                .invoiceId(invoice.getId())
                .planName(subscriptionPlan.getPlanName())
                .planPrice(subscriptionPlan.getPrice())
                .startDate(startDate)
                .endDate(endDate)
                .status(familySubscription.getStatus())
                .build();
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
    public RenewFamilySubscriptionResponse renewFamilySubscription(
            RenewFamilySubscriptionRequest request) {

        Customer customer = securityUtils.getCustomer();

        // ===== Find Family Group =====
        FamilyMember familyMember = familyMemberRepository
                .findByCustomer(customer)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.FAMILY_GROUP_NOT_FOUND));

        FamilyGroup familyGroup = familyMember.getFamilyGroup();

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

        FamilySubscription familySubscription = familySubscriptionRepository
                .findFirstByFamilyGroupOrderByIdDesc(familyGroup)
                .orElse(null);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate;

        SubscriptionInvoiceType invoiceType;

        if (familySubscription == null) {

            // ===== Đăng ký lần đầu =====
            familySubscription = new FamilySubscription();
            familySubscription.setFamilyGroup(familyGroup);

            startDate = LocalDate.now();
            endDate = startDate.plusDays(subscriptionPlan.getDurationDays());

            invoiceType = SubscriptionInvoiceType.REGISTER;

        } else {

            // ===== Gia hạn =====
            invoiceType = SubscriptionInvoiceType.RENEW;

            if (SubscriptionStatus.ACTIVE.name().equals(familySubscription.getStatus())
                    && familySubscription.getSubscriptionPlan().getId()
                    .equals(subscriptionPlan.getId())) {

                // Gia hạn cùng gói -> cộng thêm thời gian
                startDate = familySubscription.getStartDate();
                endDate = familySubscription.getEndDate()
                        .plusDays(subscriptionPlan.getDurationDays());

            } else {

                // Đổi gói hoặc gói đã hết hạn
                startDate = LocalDate.now();
                endDate = startDate.plusDays(subscriptionPlan.getDurationDays());
            }
        }

        familySubscription.setSubscriptionPlan(subscriptionPlan);
        familySubscription.setStatus(SubscriptionStatus.PENDING.name());
        familySubscription.setStartDate(startDate);
        familySubscription.setEndDate(endDate);
        familySubscription.setCanceledAt(null);

        familySubscription = familySubscriptionRepository.save(familySubscription);

        // ===== Create Invoice =====
        SubscriptionInvoice invoice = SubscriptionInvoice.builder()
                .customer(customer)
                .familySubscription(familySubscription)
                .planPrice(subscriptionPlan.getPrice())
                .status(SubscriptionInvoiceStatus.PENDING.name())
                .type(invoiceType)
                .createdAt(LocalDateTime.now())
                .build();

        subscriptionInvoiceRepository.save(invoice);

        long slotsUsed = familyMemberRepository
                .countByFamilyGroup(familyGroup);

        String inheritedTierName = customer.getCustomerTier() != null
                ? customer.getCustomerTier().getTierName()
                : null;

        return RenewFamilySubscriptionResponse.builder()
                .familySubscriptionId(familySubscription.getId())
                .planName(subscriptionPlan.getPlanName())
                .planDuration(subscriptionPlan.getDurationDays())
                .status(familySubscription.getStatus())
                .startDate(familySubscription.getStartDate())
                .endDate(familySubscription.getEndDate())
                .slotsUsed((int) slotsUsed)
                .maxSlots(subscriptionPlan.getMaxVehicleCount())
                .inheritedTierName(inheritedTierName)
                .build();
    }
}
