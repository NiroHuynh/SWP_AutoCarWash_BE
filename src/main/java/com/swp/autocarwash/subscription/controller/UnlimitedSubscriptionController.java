package com.swp.autocarwash.subscription.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.dto.response.CustomerVehicleResponse;
import com.swp.autocarwash.subscription.dto.request.RegisterUnlimitedSubscriptionRequest;
import com.swp.autocarwash.subscription.dto.response.RegisterUnlimitedSubscriptionResponse;
import com.swp.autocarwash.subscription.service.UnlimitSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class UnlimitedSubscriptionController {

    private final UnlimitSubscriptionService unlimitedSubscriptionService;

    @GetMapping("/vehicles")
    public ApiResponse<List<CustomerVehicleResponse>> getVehicles() {

        return ApiResponse.success(
                        "Vehicles retrieved successfully.",
                        unlimitedSubscriptionService.getCustomerVehicles()
                );
    }

    @PostMapping("/unlimited-subscriptions")
    public ApiResponse<RegisterUnlimitedSubscriptionResponse>
    register(
            @Valid
            @RequestBody RegisterUnlimitedSubscriptionRequest request) {

        return ApiResponse.success(
                        "Unlimited subscription registered successfully.",
                        unlimitedSubscriptionService.register(request)
                );
    }
}