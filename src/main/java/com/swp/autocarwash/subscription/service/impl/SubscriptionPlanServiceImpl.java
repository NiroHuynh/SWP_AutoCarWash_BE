package com.swp.autocarwash.subscription.service.impl;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.servicepackage.entity.ServiceCategory;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import com.swp.autocarwash.servicepackage.entity.enums.ServicePackageStatus;
import com.swp.autocarwash.servicepackage.repository.ServicePackageRepository;
import com.swp.autocarwash.subscription.dto.request.CreateSubscriptionPlanRequest;
import com.swp.autocarwash.subscription.dto.request.UpdateSubscriptionPlanRequest;
import com.swp.autocarwash.subscription.dto.response.CreateSubscriptionPlanResponse;
import com.swp.autocarwash.subscription.dto.response.CustomerSubscriptionPlanResponse;
import com.swp.autocarwash.subscription.dto.response.SubscriptionPlanDetailResponse;
import com.swp.autocarwash.subscription.dto.response.SubscriptionPlanResponse;

import com.swp.autocarwash.subscription.entity.SubscriptionPlan;
import com.swp.autocarwash.subscription.entity.enums.PlanType;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionPlanStatus;
import com.swp.autocarwash.subscription.repository.SubscriptionPlanRepository;
import com.swp.autocarwash.subscription.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final ServicePackageRepository servicePackageRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponse> getSubscriptionPlans(String status) {

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

    private SubscriptionPlanResponse toDTO(SubscriptionPlan plan) {

        return SubscriptionPlanResponse.builder()
                .planName(plan.getPlanName())
                .price(plan.getPrice())
                .durationDays(plan.getDurationDays())
                .planType(PlanType.valueOf(plan.getPlanType()))
                .description(plan.getDescription())
                .maxVehicleCount(plan.getMaxVehicleCount())
                .servicePackageName(plan.getServicePackage().getName())
                .status(plan.getStatus().name())
                .build();
    }


    @Override
    public CreateSubscriptionPlanResponse createSubscriptionPlan(CreateSubscriptionPlanRequest request) {

        validateBusiness(request);

        ServicePackage servicePackage = servicePackageRepository
                .findByIdAndStatusAndIsDeletedFalse(
                        request.getServicePackageId(),
                        ServicePackageStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.INVALID_SERVICE_PACKAGE));

        SubscriptionPlan subscriptionPlan = SubscriptionPlan.builder()
                .planName(request.getPlanName())
                .price(request.getPrice())
                .durationDays(request.getDurationDays())
                .description(request.getDescription())
                .servicePackage(servicePackage)
                .planType(request.getPlanType())
                .maxVehicleCount(
                        PlanType.valueOf(request.getPlanType()) == PlanType.UNLIMIT
                                ? 1
                                : request.getMaxVehicleCount())
                .isDeleted(false)
                .serviceCategory(
                        PlanType.valueOf(request.getPlanType()) == PlanType.UNLIMIT
                                ? ServiceCategory.builder().id(3).build()
                                : ServiceCategory.builder().id(2).build()
                )
                .build();

        subscriptionPlan = subscriptionPlanRepository.save(subscriptionPlan);

        return CreateSubscriptionPlanResponse.builder()
                .id(subscriptionPlan.getId())
                .planName(subscriptionPlan.getPlanName())
                .price(subscriptionPlan.getPrice())
                .durationDays(subscriptionPlan.getDurationDays())
                .description(subscriptionPlan.getDescription())
                .servicePackageId(servicePackage.getId())
                .servicePackageName(servicePackage.getName())
                .planType(PlanType.valueOf(subscriptionPlan.getPlanType()))
                .maxVehicleCount(subscriptionPlan.getMaxVehicleCount())
                .build();
    }

    private void validateBusiness(CreateSubscriptionPlanRequest request) {

        try{
            PlanType.valueOf(request.getPlanType());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_PLAN_TYPE);
        }

        if ( PlanType.valueOf(request.getPlanType()) == PlanType.FAMILY
                && request.getMaxVehicleCount() <= 1) {
            throw new BusinessException(ErrorCode.INVALID_FAMILY_MAX_VEHICLE_COUNT);
        }

        if ( PlanType.valueOf(request.getPlanType()) == PlanType.UNLIMIT) {
            request.setMaxVehicleCount(1);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionPlanDetailResponse getSubscriptionPlanDetail(Integer id) {

        SubscriptionPlan subscriptionPlan =
                subscriptionPlanRepository.findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

        return SubscriptionPlanDetailResponse.builder()
                .id(subscriptionPlan.getId())
                .planName(subscriptionPlan.getPlanName())
                .price(subscriptionPlan.getPrice())
                .durationDays(subscriptionPlan.getDurationDays())
                .description(subscriptionPlan.getDescription())
                .servicePackageId(subscriptionPlan.getServicePackage().getId())
                .planType(PlanType.valueOf(subscriptionPlan.getPlanType()))
                .maxVehicleCount(subscriptionPlan.getMaxVehicleCount())
                .status(subscriptionPlan.getStatus())
                .build();
    }

    @Override
    public void updateSubscriptionPlan(Integer id,
                                       UpdateSubscriptionPlanRequest request) {

        SubscriptionPlan subscriptionPlan =
                subscriptionPlanRepository.findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

        ServicePackage servicePackage =
                servicePackageRepository.findByIdAndStatus(
                                request.getServicePackageId(),
                                ServicePackageStatus.ACTIVE)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.INVALID_SERVICE_PACKAGE));

        validateBusiness(request);

        subscriptionPlan.setPlanName(request.getPlanName());
        subscriptionPlan.setPrice(request.getPrice());
        subscriptionPlan.setDurationDays(request.getDurationDays());
        subscriptionPlan.setDescription(request.getDescription());
        subscriptionPlan.setServicePackage(servicePackage);
        subscriptionPlan.setPlanType(request.getPlanType());
        subscriptionPlan.setMaxVehicleCount(request.getMaxVehicleCount());
        subscriptionPlan.setStatus(SubscriptionPlanStatus.valueOf(request.getStatus()));

        // KHÔNG cập nhật serviceCategory theo AC04

        subscriptionPlanRepository.save(subscriptionPlan);
    }

    private void validateBusiness(UpdateSubscriptionPlanRequest request) {

        if(request.getMaxVehicleCount()==null || request.getMaxVehicleCount() <= 0){
            throw new BusinessException(ErrorCode.INVALID_MAX_VEHICLE_COUNT);
        }

        try{
            ServicePackageStatus.valueOf(request.getStatus());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_STATUS);
        }

        try{
            PlanType.valueOf(request.getPlanType());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_PLAN_TYPE);
        }

        if (PlanType.valueOf(request.getPlanType()) == PlanType.FAMILY) {

            if (request.getMaxVehicleCount() == null
                    || request.getMaxVehicleCount() <= 1) {
                throw new BusinessException(
                        ErrorCode.INVALID_FAMILY_MAX_VEHICLE_COUNT);
            }
        }

        if (PlanType.valueOf(request.getPlanType()) == PlanType.UNLIMIT) {

            if (request.getMaxVehicleCount() != null
                    && request.getMaxVehicleCount() != 1) {
                throw new BusinessException(
                        ErrorCode.INVALID_UNLIMITED_MAX_VEHICLE_COUNT);
            }
        }
    }

    @Transactional
    public void deleteSubscriptionPlan(Integer id) {

        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

        if (subscriptionPlan.getStatus() == SubscriptionPlanStatus.INACTIVE) {
            throw new BusinessException(
                    ErrorCode.SUBSCRIPTION_PLAN_ALREADY_INACTIVE);
        }

        subscriptionPlan.setStatus(SubscriptionPlanStatus.INACTIVE);
        subscriptionPlan.setIsDeleted(true);

        subscriptionPlanRepository.save(subscriptionPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerSubscriptionPlanResponse> getActiveSubscriptionPlans() {

        List<SubscriptionPlan> subscriptionPlans =
                subscriptionPlanRepository.findByStatusAndIsDeletedFalse(
                        SubscriptionPlanStatus.ACTIVE
                );

        return subscriptionPlans.stream()
                .map(this::toCustomerResponse)
                .toList();
    }

    private CustomerSubscriptionPlanResponse toCustomerResponse(
            SubscriptionPlan subscriptionPlan
    ) {

        return CustomerSubscriptionPlanResponse.builder()
                .id(subscriptionPlan.getId())
                .planName(subscriptionPlan.getPlanName())
                .price(subscriptionPlan.getPrice())
                .durationDays(subscriptionPlan.getDurationDays())
                .planType(PlanType.valueOf(subscriptionPlan.getPlanType()))
                .servicePackageName(subscriptionPlan.getServicePackage().getName())
                .maxVehicleCount(subscriptionPlan.getMaxVehicleCount())
                .description(subscriptionPlan.getDescription())
                .build();
    }
}
