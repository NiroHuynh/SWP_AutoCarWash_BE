package com.swp.autocarwash.subscription.repository;

import com.swp.autocarwash.subscription.entity.SubscriptionPlan;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer> {

    List<SubscriptionPlan> findByStatus(SubscriptionPlanStatus status);

    List<SubscriptionPlan> findByStatusInAndIsDeletedFalse(List<SubscriptionPlanStatus> statuses);

    Optional<SubscriptionPlan> findByIdAndIsDeletedFalse(Integer id);

    boolean existsByPlanNameIgnoreCaseAndIdNot(String planName, Integer id);

    /**
     * Kiểm tra còn subscription plan chưa bị xóa sử dụng service package hay không.
     */
    boolean existsByServicePackage_IdAndIsDeletedFalse(Integer servicePackageId);

    List<SubscriptionPlan> findByStatusAndIsDeletedFalse(
            SubscriptionPlanStatus status
    );

    Optional<SubscriptionPlan> findByIdAndStatusAndIsDeletedFalse(
            Integer id,
            SubscriptionPlanStatus status
    );

    @Query("""
            SELECT sp
            FROM SubscriptionPlan sp
            LEFT JOIN FETCH sp.servicePackage
            WHERE sp.planType = 'FAMILY'
            AND sp.isDeleted = false
            AND sp.status = SubscriptionPlanStatus.ACTIVE
            ORDER BY sp.price
            """)
    List<SubscriptionPlan> findAllFamilyPlans();


}