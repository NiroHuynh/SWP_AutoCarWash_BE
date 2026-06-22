package com.swp.autocarwash.subscription.service;

public interface FamilySubscriptionService {

    /**
     * Lấy service package đang active của vehicle trong family subscription.
     *
     * Quy trình:
     * - Nhận vehicleId cần kiểm tra.
     * - Tìm family member chứa vehicle này.
     * - Xác định family group tương ứng.
     * - Kiểm tra family subscription đang active.
     * - Lấy service package id từ subscription plan.
     *
     * @param vehicleId id của vehicle
     * @return servicePackageId nếu vehicle có family subscription active,
     *         null nếu không tồn tại
     *
     * @Author Phong
     */
    Integer getActiveServicePackageId(Long vehicleId);
}
