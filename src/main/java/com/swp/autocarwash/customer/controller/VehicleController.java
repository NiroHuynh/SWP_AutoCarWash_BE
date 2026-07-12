package com.swp.autocarwash.customer.controller;

import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.dto.request.CreateVehicleRequest;
import com.swp.autocarwash.customer.dto.request.UpdateVehicleRequest;
import com.swp.autocarwash.customer.dto.response.CreateVehicleResponse;
import com.swp.autocarwash.customer.dto.response.UpdateVehicleResponse;
import com.swp.autocarwash.customer.service.vehicle.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


/**
 *
 * REST API xử lý vehicle
 *
 * @author Phong
 * @version 1.0
 */
@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {



    private final VehicleService vehicleService;

    private final SecurityUtils securityUtils;


    /**
     *
     * API tạo vehicle mới
     *
     * POST /api/vehicles
     *
     * @param request vehicle information
     * @return created vehicle
     */
    @PostMapping
    public ApiResponse<CreateVehicleResponse> createVehicle(
            @Valid
            @RequestBody
            CreateVehicleRequest request
    ){

        Long userId = securityUtils.getCurrentUserId();

        CreateVehicleResponse response =
                vehicleService
                        .createVehicle(userId, request);

        return ApiResponse.success(
                        "Vehicle added successfully",
                        response
                );

    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<?>> deleteVehicle(@AuthenticationPrincipal UserCustomerDetails userDetails,
                                                        @PathVariable("vehicleId") Long id){
        Long customerId = userDetails.getCustomerId();

        vehicleService.deleteVehicle(customerId,id);
        return ResponseEntity.ok(ApiResponse.success("Vehicle deleted successfully",null));
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<UpdateVehicleResponse>> updateVehicle(@AuthenticationPrincipal UserCustomerDetails userDetails,
                                                                            @PathVariable("vehicleId") Long id,
                                                                            @RequestBody UpdateVehicleRequest request){
        Long customerId = userDetails.getCustomerId();
        UpdateVehicleResponse response = vehicleService.updateVehicle(customerId, id, request);

        return ResponseEntity.ok(ApiResponse.success("Vehicle updated successfully", response));
    }


}
