package com.swp.autocarwash.subscription.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.subscription.dto.request.CreateSubscriptionPlanRequest;
import com.swp.autocarwash.subscription.dto.request.UpdateSubscriptionPlanRequest;
import com.swp.autocarwash.subscription.dto.response.CreateSubscriptionPlanResponse;
import com.swp.autocarwash.subscription.dto.response.SubscriptionPlanDetailResponse;
import com.swp.autocarwash.subscription.dto.response.SubscriptionPlanResponse;
import com.swp.autocarwash.subscription.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping("/api/admin/subscription-plans")
    public ApiResponse<List<SubscriptionPlanResponse>> getSubscriptionPlans(
            @RequestParam(defaultValue = "ALL") String status) {

        List<SubscriptionPlanResponse> response =
                subscriptionPlanService.getSubscriptionPlans(status);

        return ApiResponse.success(
                "Subscription plans retrieved successfully.",
                response
        );
    }


    @PostMapping("/api/admin/subscription-plans")
    public ApiResponse<CreateSubscriptionPlanResponse> createSubscriptionPlan(
            @Valid @RequestBody CreateSubscriptionPlanRequest request) {

        return ApiResponse.<CreateSubscriptionPlanResponse>builder()
                .success(true)
                .message("Subscription plan created successfully.")
                .data(subscriptionPlanService.createSubscriptionPlan(request))
                .build();
    }

    @GetMapping("/api/admin/subscription-plans/{id}")
    public ApiResponse<SubscriptionPlanDetailResponse>
    getSubscriptionPlanDetail(@PathVariable Integer id) {

        return ApiResponse.success(
                "Subscription plan retrieved successfully.",
                subscriptionPlanService.getSubscriptionPlanDetail(id)
        );
    }

    @PutMapping("/api/admin/subscription-plans/{id}")
    public ApiResponse<Object> updateSubscriptionPlan(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateSubscriptionPlanRequest request) {

        subscriptionPlanService.updateSubscriptionPlan(id, request);

        return ApiResponse.success(
                "Subscription plan updated successfully.",
                null
        );
    }

    @DeleteMapping("/api/admin/subscription-plans/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteSubscriptionPlan(
            @PathVariable Integer id) {

        subscriptionPlanService.deleteSubscriptionPlan(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Subscription plan deleted successfully.",
                        null
                )
        );
    }
}
