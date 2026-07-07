package com.swp.autocarwash.subscription.service.impl;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.subscription.dto.response.SubscriptionPlanResponseDTO;
import com.swp.autocarwash.subscription.entity.SubscriptionPlan;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionPlanStatus;
import com.swp.autocarwash.subscription.repository.SubscriptionPlanRepository;
import com.swp.autocarwash.subscription.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public List<SubscriptionPlanResponseDTO> getSubscriptionPlans(String status) {

        List<SubscriptionPlan> subscriptionPlans;

        switch (status.toUpperCase()) {

            case "ALL":
                subscriptionPlans = subscriptionPlanRepository.findByStatusIn(
                        List.of(
                                SubscriptionPlanStatus.ACTIVE,
                                SubscriptionPlanStatus.INACTIVE
                        )
                );
                break;

            case "ACTIVE":
                subscriptionPlans = subscriptionPlanRepository.findByStatus(
                        SubscriptionPlanStatus.ACTIVE
                );
                break;

            case "INACTIVE":
                subscriptionPlans = subscriptionPlanRepository.findByStatus(
                        SubscriptionPlanStatus.INACTIVE
                );
                break;

            default:
                throw new BusinessException(ErrorCode.INVALID_SUBSCRIPTION_PLAN_STATUS);
        }

        return subscriptionPlans.stream()
                .map(this::toDTO)
                .toList();
    }

    private SubscriptionPlanResponseDTO toDTO(SubscriptionPlan plan) {

        return SubscriptionPlanResponseDTO.builder()
                .planName(plan.getPlanName())
                .price(plan.getPrice())
                .durationDays(plan.getDurationDays())
                .planType(plan.getPlanType())
                .description(plan.getDescription())
                .maxVehicleCount(plan.getMaxVehicleCount())
                .servicePackageName(plan.getServicePackage().getName())
                .status(plan.getStatus().name())
                .build();
    }

}
