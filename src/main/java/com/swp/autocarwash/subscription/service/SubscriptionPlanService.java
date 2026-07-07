package com.swp.autocarwash.subscription.service;

import com.swp.autocarwash.subscription.dto.response.SubscriptionPlanResponseDTO;

import java.util.List;

public interface SubscriptionPlanService {

    List<SubscriptionPlanResponseDTO> getSubscriptionPlans(String status);

}
