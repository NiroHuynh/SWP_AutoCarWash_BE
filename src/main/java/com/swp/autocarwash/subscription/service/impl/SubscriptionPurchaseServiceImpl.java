package com.swp.autocarwash.subscription.service.impl;

import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.payment.dto.request.SubscriptionPaymentInitRequest;
import com.swp.autocarwash.payment.dto.response.SubscriptionPaymentInitResponse;
import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceType;
import com.swp.autocarwash.payment.repository.SubscriptionInvoiceRepository;
import com.swp.autocarwash.payment.service.SubscriptionPaymentService;
import com.swp.autocarwash.payment.service.impl.SubscriptionPaymentServiceImpl;
import com.swp.autocarwash.subscription.dto.request.RegisterUnlimitedSubscriptionRequest;
import com.swp.autocarwash.subscription.entity.SubscriptionPlan;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.entity.enums.PlanType;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionPlanStatus;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import com.swp.autocarwash.subscription.repository.SubscriptionPlanRepository;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import com.swp.autocarwash.subscription.service.SubscriptionPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Triển khai luồng mua/gia hạn gói Unlimited có thanh toán (FE-US-56-04).
 *
 * <p>Chỉ dùng các method sẵn có / kế thừa JpaRepository của Phong
 * ({@code findByIdAndStatusAndIsDeletedFalse}, {@code existsByVehicleIdAndStatus},
 * {@code findById}, {@code save}) — không sửa code Phong.</p>
 *
 * @author Ngân
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class SubscriptionPurchaseServiceImpl implements SubscriptionPurchaseService {

    private final SecurityUtils securityUtils;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final VehicleRepository vehicleRepository;
    private final UnlimitSubscriptionRepository unlimitSubscriptionRepository;
    private final SubscriptionInvoiceRepository subscriptionInvoiceRepository;
    private final SubscriptionPaymentService subscriptionPaymentService;

    @Override
    @Transactional
    public SubscriptionPaymentInitResponse registerUnlimited(RegisterUnlimitedSubscriptionRequest request) {
        Customer customer = securityUtils.getCustomer();
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        SubscriptionPlan plan = subscriptionPlanRepository
                .findByIdAndStatusAndIsDeletedFalse(request.getSubscriptionPlanId(), SubscriptionPlanStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_SUBSCRIPTION_PLAN));

        if (!PlanType.UNLIMIT.name().equals(plan.getPlanType())) {
            throw new BusinessException(ErrorCode.INVALID_SUBSCRIPTION_PLAN);
        }

        Vehicle vehicle = vehicleRepository
                .findByIdAndCustomerIdAndIsDeletedFalse(request.getVehicleId(), customer.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.VEHICLE_NOT_FOUND));

        if (unlimitSubscriptionRepository.existsByVehicleIdAndStatus(vehicle.getId(), SubscriptionStatus.ACTIVE)) {
            throw new BusinessException(ErrorCode.VEHICLE_ALREADY_SUBSCRIBED);
        }

        // Idempotent: xe đã có hóa đơn PENDING đang chờ -> trả lại QR đó, không tạo trùng.
        SubscriptionInvoice existing = subscriptionInvoiceRepository
                .findFirstByUnlimitSubscription_Vehicle_IdAndStatusOrderByCreatedAtDesc(
                        vehicle.getId(), SubscriptionPaymentServiceImpl.INVOICE_PENDING)
                .orElse(null);
        if (existing != null) {
            return subscriptionPaymentService.getInvoiceStatus(existing.getId(), customer.getId());
        }

        // Tạo gói PENDING (chưa hưởng quyền lợi tới khi thanh toán). startDate/endDate tạm để thoả
        // @NotNull; listener tính lại theo ngày thanh toán khi kích hoạt.
        UnlimitSubscription subscription = UnlimitSubscription.builder()
                .subscriptionPlan(plan)
                .vehicle(vehicle)
                .customer(customer)
                .status(SubscriptionStatus.PENDING)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(plan.getDurationDays()))
                .build();
        unlimitSubscriptionRepository.save(subscription);

        return subscriptionPaymentService.initiatePayment(
                SubscriptionPaymentInitRequest.builder()
                        .customerId(customer.getId())
                        .planPrice(plan.getPrice())
                        .planName(plan.getPlanName())
                        .unlimitSubscriptionId(subscription.getId())
                        .build());
    }

    @Override
    @Transactional
    public SubscriptionPaymentInitResponse renewUnlimited(Long subscriptionId) {
        Customer customer = securityUtils.getCustomer();
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        UnlimitSubscription subscription = unlimitSubscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        if (!subscription.getVehicle().getCustomer().getId().equals(customer.getId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        // Chỉ ACTIVE mới được gia hạn (RENEW.docx AC03).
        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.INVALID_SUBSCRIPTION_STATUS);
        }

        // Phòng hờ trạng thái stale: endDate đã qua nhưng status chưa kịp chuyển EXPIRED.
        if (subscription.getEndDate().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.SUBSCRIPTION_EXPIRED);
        }

        // Chỉ cho renew khi còn <=3 ngày trước hạn (RENEW.docx AC01).
        if (subscription.getEndDate().isAfter(LocalDate.now().plusDays(3))) {
            throw new BusinessException(ErrorCode.RENEWAL_NOT_AVAILABLE);
        }

        // Idempotent: đã có hóa đơn gia hạn PENDING -> trả lại QR cũ.
        SubscriptionInvoice existing = subscriptionInvoiceRepository
                .findFirstByUnlimitSubscription_IdAndStatusOrderByCreatedAtDesc(
                        subscription.getId(), SubscriptionPaymentServiceImpl.INVOICE_PENDING)
                .orElse(null);
        if (existing != null) {
            return subscriptionPaymentService.getInvoiceStatus(existing.getId(), customer.getId());
        }

        SubscriptionPlan plan = subscription.getSubscriptionPlan();
        return subscriptionPaymentService.initiatePayment(
                SubscriptionPaymentInitRequest.builder()
                        .customerId(customer.getId())
                        .planPrice(plan.getPrice())
                        .planName(plan.getPlanName())
                        .unlimitSubscriptionId(subscription.getId())
                        .type(SubscriptionInvoiceType.RENEW)
                        .build());
    }
}
