package com.swp.autocarwash.subscription.service.impl;

import com.swp.autocarwash.subscription.dto.response.ActiveSubscriptionResponse;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.entity.SubscriptionPlan;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.repository.FamilySubscriptionRepository;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import com.swp.autocarwash.subscription.service.SubscriptionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Triển khai {@link SubscriptionQueryService}.
 *
 * @author Ngân
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final UnlimitSubscriptionRepository unlimitSubscriptionRepository;
    private final FamilySubscriptionRepository familySubscriptionRepository;

    @Override
    @Transactional(readOnly = true)
    public ActiveSubscriptionResponse getActiveSubscription(Long customerId) {
        return unlimitSubscriptionRepository.findActiveByCustomerId(customerId)
                .map(this::toResponse)
                .orElseGet(() -> familySubscriptionRepository.findActiveByCustomerId(customerId)
                        .map(this::toResponse)
                        .orElse(null));
    }

    private ActiveSubscriptionResponse toResponse(UnlimitSubscription subscription) {
        SubscriptionPlan plan = subscription.getSubscriptionPlan();
        return buildResponse(plan, subscription.getStartDate(), subscription.getEndDate());
    }

    private ActiveSubscriptionResponse toResponse(FamilySubscription subscription) {
        SubscriptionPlan plan = subscription.getSubscriptionPlan();
        return buildResponse(plan, subscription.getStartDate(), subscription.getEndDate());
    }

    private ActiveSubscriptionResponse buildResponse(SubscriptionPlan plan, LocalDate startDate, LocalDate endDate) {
        return ActiveSubscriptionResponse.builder()
                .planName(plan.getPlanName())
                .planType(plan.getPlanType())
                .startDate(startDate)
                .expiryDate(endDate)
                .daysRemaining(ChronoUnit.DAYS.between(LocalDate.now(), endDate))
                .build();
    }
}
