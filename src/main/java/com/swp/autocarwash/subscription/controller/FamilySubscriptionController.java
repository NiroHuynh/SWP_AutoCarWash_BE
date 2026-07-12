package com.swp.autocarwash.subscription.controller;


import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.subscription.dto.request.RegisterFamilySubscriptionRequest;
import com.swp.autocarwash.subscription.dto.response.FamilySubscriptionPlansResponse;
import com.swp.autocarwash.subscription.dto.response.RegisterFamilySubscriptionResponse;
import com.swp.autocarwash.subscription.service.FamilySubscriptionService;
import com.swp.autocarwash.subscription.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions/family")
@RequiredArgsConstructor
public class FamilySubscriptionController {

    private final SubscriptionPlanService subscriptionPlanService;
    private final FamilySubscriptionService familySubscriptionService;


    @GetMapping("/plans")
    public ApiResponse<FamilySubscriptionPlansResponse> getFamilyPlans() {

        return ApiResponse.success(
                "Get family subscription plans successfully.",
                subscriptionPlanService.getFamilySubscriptionPlans()
        );
    }

    @PostMapping
    public ApiResponse<RegisterFamilySubscriptionResponse> registerFamilySubscription(
            @Valid @RequestBody RegisterFamilySubscriptionRequest request) {

        return ApiResponse.success(
                "Family subscription registered successfully.",
                familySubscriptionService.registerFamilySubscription(request)
        );
    }

    @PatchMapping("/cancel")
    public ApiResponse<Void> cancelFamilySubscription() {

        familySubscriptionService.cancelFamilySubscription();

        return ApiResponse.success(
                "Family subscription cancelled successfully.",
                null
        );
    }
}
