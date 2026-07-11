package com.swp.autocarwash.servicepackage.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.servicepackage.dto.request.CreateServicePackageRequest;
import com.swp.autocarwash.servicepackage.dto.request.UpdateServicePackageRequest;
import com.swp.autocarwash.servicepackage.dto.response.ServicePackageResponse;
import com.swp.autocarwash.servicepackage.service.ServicePackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-packages")
@RequiredArgsConstructor
public class ServicePackageController {

    private final ServicePackageService servicePackageService;

    /**
     * API-14-04: Lấy danh sách service package (Admin)
     * AC-14.4.4: data rỗng → trả [] bình thường, FE tự hiện trạng thái trống
     */
    @GetMapping
    public ApiResponse<List<ServicePackageResponse>> getAllServicePackages() {

        List<ServicePackageResponse> data =
                servicePackageService.getAllServicePackages();

        return ApiResponse.success(
                "Get service packages successfully",
                data
        );
    }

    /**
     * API-14-01: Tạo service package mới
     * AC-14.1.1: trả về object vừa tạo kèm durationMinutes
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<ServicePackageResponse> createServicePackage(
            @RequestBody CreateServicePackageRequest request
    ) {

        ServicePackageResponse data =
                servicePackageService.createServicePackage(request);

        return ApiResponse.success(
                "Service package created successfully",
                data
        );
    }

    @PutMapping("/{servicePackageId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<ServicePackageResponse> updateServicePackage(
            @PathVariable Integer servicePackageId,
            @Valid @RequestBody UpdateServicePackageRequest request) {

        return ApiResponse.success(
                "Service package updated successfully",
                servicePackageService.updateServicePackage(servicePackageId, request)
        );
    }

    @DeleteMapping("/{servicePackageId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<Void> deleteServicePackage(
            @PathVariable Integer servicePackageId) {

        servicePackageService.deleteServicePackage(servicePackageId);

        return ApiResponse.success(
                "Service package deleted successfully",
                null
        );
    }
}