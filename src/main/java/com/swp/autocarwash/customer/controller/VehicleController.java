package com.swp.autocarwash.customer.controller;

import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.dto.request.CreateVehicleRequest;
import com.swp.autocarwash.customer.dto.response.CreateVehicleResponse;
import com.swp.autocarwash.customer.service.vehicle.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

}
