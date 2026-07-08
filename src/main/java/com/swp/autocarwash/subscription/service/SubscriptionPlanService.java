package com.swp.autocarwash.subscription.service;

import com.swp.autocarwash.subscription.dto.request.CreateSubscriptionPlanRequest;
import com.swp.autocarwash.subscription.dto.request.UpdateSubscriptionPlanRequest;
import com.swp.autocarwash.subscription.dto.response.CreateSubscriptionPlanResponse;
import com.swp.autocarwash.subscription.dto.response.CustomerSubscriptionPlanResponse;
import com.swp.autocarwash.subscription.dto.response.SubscriptionPlanDetailResponse;
import com.swp.autocarwash.subscription.dto.response.SubscriptionPlanResponse;

import java.util.List;

public interface SubscriptionPlanService {

    List<SubscriptionPlanResponse> getSubscriptionPlans(String status);

    CreateSubscriptionPlanResponse createSubscriptionPlan(
            CreateSubscriptionPlanRequest request
    );

    SubscriptionPlanDetailResponse getSubscriptionPlanDetail(Integer id);

    void updateSubscriptionPlan(Integer id,
                                UpdateSubscriptionPlanRequest request);

    void deleteSubscriptionPlan(Integer id);

    List<CustomerSubscriptionPlanResponse> getActiveSubscriptionPlans();
}
