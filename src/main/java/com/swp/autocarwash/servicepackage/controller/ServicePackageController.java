package com.swp.autocarwash.servicepackage.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.servicepackage.dto.response.ServicePackageResponse;
import com.swp.autocarwash.servicepackage.service.ServicePackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServicePackageController {

    private final ServicePackageService servicePackageService;

    @GetMapping("/api/admin/service-packages/active")
    public ApiResponse<List<ServicePackageResponse>> getActiveServicePackages() {

        return ApiResponse.<List<ServicePackageResponse>>builder()
                .success(true)
                .message("Active service packages retrieved successfully.")
                .data(servicePackageService.getActiveServicePackages())
                .build();
    }
}
