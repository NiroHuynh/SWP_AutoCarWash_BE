package com.swp.autocarwash.subscription.service;

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
}
