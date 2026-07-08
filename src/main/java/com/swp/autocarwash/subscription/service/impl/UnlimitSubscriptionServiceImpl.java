package com.swp.autocarwash.subscription.service.impl;

import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.dto.response.CustomerVehicleResponse;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceStatus;
import com.swp.autocarwash.payment.repository.SubscriptionInvoiceRepository;
import com.swp.autocarwash.subscription.dto.request.RegisterUnlimitedSubscriptionRequest;
import com.swp.autocarwash.subscription.dto.response.RegisterUnlimitedSubscriptionResponse;
import com.swp.autocarwash.subscription.entity.SubscriptionPlan;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.entity.enums.PlanType;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionPlanStatus;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import com.swp.autocarwash.subscription.repository.SubscriptionPlanRepository;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import com.swp.autocarwash.subscription.service.UnlimitSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnlimitSubscriptionServiceImpl implements UnlimitSubscriptionService {

    private final UnlimitSubscriptionRepository unlimitSubscriptionRepository;
    private final SecurityUtils securityUtils;
    private final VehicleRepository vehicleRepository;
    private final SubscriptionInvoiceRepository subscriptionInvoiceRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;


    @Override
    public Integer getActiveServicePackageId(Long vehicleId) {
        if(vehicleId==null){
            return null;
        }

        return unlimitSubscriptionRepository.findActiveServicePackageIdByVehicleId(vehicleId);
    }

    @Override
    public boolean hasUnlimitedSubscription(Long vehicle, Integer servicePackageId) {
        return unlimitSubscriptionRepository.existsActiveUnlimitSubscription(vehicle, servicePackageId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerVehicleResponse> getCustomerVehicles() {

        Customer customer = securityUtils.getCustomer();

        return vehicleRepository
                .findByCustomerIdAndIsDeletedFalse(customer.getId())
                .stream()
                .map(vehicle ->
                        CustomerVehicleResponse.builder()
                                .id(vehicle.getId())
                                .licensePlate(vehicle.getLicensePlate())
                                .vehicleName(vehicle.getBrandName())
                                .build())
                .toList();
    }

    @Override
    @Transactional
    public RegisterUnlimitedSubscriptionResponse register(
            RegisterUnlimitedSubscriptionRequest request) {

        Customer customer = securityUtils.getCustomer();

        SubscriptionPlan subscriptionPlan =
                subscriptionPlanRepository
                        .findByIdAndStatusAndIsDeletedFalse(
                                request.getSubscriptionPlanId(),
                                SubscriptionPlanStatus.ACTIVE)
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.INVALID_SUBSCRIPTION_PLAN));

        if (!PlanType.UNLIMIT.name().equals(subscriptionPlan.getPlanType())) {
            throw new BusinessException(ErrorCode.INVALID_SUBSCRIPTION_PLAN);
        }

        Vehicle vehicle =
                vehicleRepository
                        .findByIdAndCustomerIdAndIsDeletedFalse(
                                request.getVehicleId(),
                                customer.getId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.VEHICLE_NOT_FOUND));

        if (unlimitSubscriptionRepository.existsByVehicleIdAndStatus(
                vehicle.getId(),
                SubscriptionStatus.ACTIVE)) {

            throw new BusinessException(
                    ErrorCode.VEHICLE_ALREADY_SUBSCRIBED);
        }

        UnlimitSubscription subscription =
                UnlimitSubscription.builder()
                        .subscriptionPlan(subscriptionPlan)
                        .vehicle(vehicle)
                        .status(SubscriptionStatus.ACTIVE)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(subscriptionPlan.getDurationDays()))
                        .customer(customer)
                        .build();

        unlimitSubscriptionRepository.save(subscription);

        SubscriptionInvoice invoice =
                SubscriptionInvoice.builder()
                        .unlimitSubscription(subscription)
                        .planPrice(subscriptionPlan.getPrice())
                        .planPrice(subscriptionPlan.getPrice())
                        .status(SubscriptionInvoiceStatus.PENDING.name())
                        .customer(customer)
                        .build();

        subscriptionInvoiceRepository.save(invoice);

        return RegisterUnlimitedSubscriptionResponse.builder()
                .subscriptionId(subscription.getId())
                .invoiceId(invoice.getId())
                .status(subscription.getStatus())
                .build();
    }

    @Override
    @Transactional
    public void cancelSubscription(Long subscriptionId) {

        Customer customer = securityUtils.getCustomer();

        UnlimitSubscription subscription =
                unlimitSubscriptionRepository
                        .findById(subscriptionId)
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.SUBSCRIPTION_NOT_FOUND));

        // Kiểm tra quyền sở hữu
        if (!subscription.getVehicle()
                .getCustomer()
                .getId()
                .equals(customer.getId())) {

            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        // Chỉ ACTIVE mới được hủy
        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {

            throw new BusinessException(
                    ErrorCode.INVALID_SUBSCRIPTION_STATUS);
        }

        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscription.setCanceledAt(LocalDateTime.now());

        unlimitSubscriptionRepository.save(subscription);
    }
}
