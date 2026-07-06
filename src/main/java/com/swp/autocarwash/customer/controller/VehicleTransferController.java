package com.swp.autocarwash.customer.controller;

import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.dto.request.TransferPlanRequest;
import com.swp.autocarwash.customer.service.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions/transfer")
public class VehicleTransferController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> transferSubscription
            (@RequestBody TransferPlanRequest request,
             @AuthenticationPrincipal UserCustomerDetails userDetails){
        Long customerId = userDetails.getCustomerId();
        vehicleService.transferSubscription(customerId,request);
        return ResponseEntity.ok(ApiResponse.success("Plan transferred successfully.", null));
    }

}
