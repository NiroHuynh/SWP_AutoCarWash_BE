package com.swp.autocarwash.subscription.repository;

import com.swp.autocarwash.subscription.entity.SubscriptionPlan;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer> {

    List<SubscriptionPlan> findByStatus(SubscriptionPlanStatus status);

    List<SubscriptionPlan> findByStatusIn(List<SubscriptionPlanStatus> statuses);
}