package com.swp.autocarwash.subscription.service;

import com.swp.autocarwash.customer.dto.response.CustomerVehicleResponse;
import com.swp.autocarwash.subscription.dto.request.RegisterUnlimitedSubscriptionRequest;
import com.swp.autocarwash.subscription.dto.response.RegisterUnlimitedSubscriptionResponse;

import java.util.List;

/**
 *
 * khai báo các hàm của unlimitSubscriptionService
 *
 * @Author Phong
 */
public interface UnlimitSubscriptionService {

    /**
     *
     * lấy gói unlimit nếu có của vehicle
     *
     * @Author Phong
     */
    Integer getActiveServicePackageId(Long vehicleId);

    /**
     *
     * kiểm tra xemm xe này có gói unlimit đó không
     *
     * @Author Phong
     */
    boolean hasUnlimitedSubscription(Long vehicle, Integer servicePackageId);

    List<CustomerVehicleResponse> getCustomerVehicles();

    RegisterUnlimitedSubscriptionResponse register(
            RegisterUnlimitedSubscriptionRequest request);

    void cancelSubscription(Long subscriptionId);
}
