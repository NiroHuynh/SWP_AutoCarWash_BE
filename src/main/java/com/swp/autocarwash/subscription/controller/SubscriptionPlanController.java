package com.swp.autocarwash.subscription.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.subscription.dto.response.SubscriptionPlanResponseDTO;
import com.swp.autocarwash.subscription.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping("/admin/subscription-plans")
    public ApiResponse<List<SubscriptionPlanResponseDTO>> getSubscriptionPlans(
            @RequestParam(defaultValue = "ALL") String status) {

        List<SubscriptionPlanResponseDTO> response =
                subscriptionPlanService.getSubscriptionPlans(status);

        return ApiResponse.success(
                "Subscription plans retrieved successfully.",
                response
        );
    }

}
