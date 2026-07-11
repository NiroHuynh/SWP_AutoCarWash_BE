package com.swp.autocarwash.subscription.repository;

import com.swp.autocarwash.subscription.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository
        extends JpaRepository<SubscriptionPlan, Integer> {

    /**
     * Kiểm tra còn subscription plan chưa bị xóa sử dụng service package hay không.
     */
    boolean existsByServicePackage_IdAndIsDeletedFalse(Integer servicePackageId);

}