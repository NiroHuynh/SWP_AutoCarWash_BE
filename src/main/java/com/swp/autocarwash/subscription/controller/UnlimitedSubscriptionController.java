package com.swp.autocarwash.subscription.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.dto.response.CustomerVehicleResponse;
import com.swp.autocarwash.subscription.dto.request.RegisterUnlimitedSubscriptionRequest;
import com.swp.autocarwash.subscription.dto.request.TransferVehicleRequest;
import com.swp.autocarwash.subscription.dto.response.RegisterUnlimitedSubscriptionResponse;
import com.swp.autocarwash.subscription.dto.response.UnlimitedSubscriptionResponse;
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

    @PatchMapping("/unlimited-subscriptions/{id}/cancel")
    public ApiResponse<Object> cancelSubscription(
            @PathVariable Long id) {

        unlimitedSubscriptionService.cancelSubscription(id);

        return ApiResponse.success(
                        "Subscription canceled successfully.",
                        null
                );
    }

    @GetMapping("unlimited-subscriptions/{id}/available-vehicles")
    public ApiResponse<List<CustomerVehicleResponse>>
    getAvailableVehicles(@PathVariable Long id) {

        return ApiResponse.success(
                        "Available vehicles retrieved successfully.",
                        unlimitedSubscriptionService.getAvailableVehicles(id)
                );
    }

    @PatchMapping("/unlimited-subscriptions/{id}/transfer-vehicle")
    public ApiResponse<Object> transferVehicle(
            @PathVariable Long id,
            @Valid @RequestBody TransferVehicleRequest request) {

        unlimitedSubscriptionService.transferVehicle(id, request);

        return ApiResponse.success(
                        "Vehicle transferred successfully.",
                        null
                );
    }

    @GetMapping("/unlimited-subscriptions")
    public ApiResponse<List<UnlimitedSubscriptionResponse>> getMySubscriptions() {

        List<UnlimitedSubscriptionResponse> response =
                unlimitedSubscriptionService.getMySubscriptions();

        String message = response.isEmpty()
                ? "No unlimited subscriptions found."
                : "Unlimited subscriptions retrieved successfully.";

        return ApiResponse.success(
                        message,
                        response
                );
    }
}