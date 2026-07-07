package com.swp.autocarwash.servicepackage.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.servicepackage.dto.request.CreateAddonServiceRequest;
import com.swp.autocarwash.servicepackage.dto.request.UpdateAddonServiceRequest;
import com.swp.autocarwash.servicepackage.dto.response.AddonServiceResponse;
import com.swp.autocarwash.servicepackage.service.AddonServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addon-services")
@RequiredArgsConstructor
public class AddonServiceController {

    private final AddonServiceService addonServiceService;

    /**
     * API-15-04: Lấy danh sách addon service cho Admin
     * Role ADMIN — sai quyền sẽ bị Spring Security chặn trả 401/403
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<AddonServiceResponse>> getAllAddonServices() {

        List<AddonServiceResponse> data =
                addonServiceService.getAllAddonServices();

        return ApiResponse.success(
                "Get addon services successfully",
                data
        );
    }

    /**
     * API-15-01: Tạo addon service mới
     * AC-15.1.1: trả về object vừa tạo để FE append vào list
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<AddonServiceResponse> createAddonService(
            @RequestBody CreateAddonServiceRequest request
    ) {

        AddonServiceResponse data =
                addonServiceService.createAddonService(request);

        return ApiResponse.success(
                "Addon service created successfully",
                data
        );
    }

    /**
     * API-15-02: Cập nhật addon service (Admin)
     * AC-15.2.1: trả về object đã cập nhật để FE replace trong list
     */
    @PutMapping("/{addonServiceId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<AddonServiceResponse> updateAddonService(
            @PathVariable Integer addonServiceId,
            @RequestBody UpdateAddonServiceRequest request
    ) {

        AddonServiceResponse data =
                addonServiceService.updateAddonService(addonServiceId, request);

        return ApiResponse.success(
                "Addon service updated successfully",
                data
        );
    }

    /**
     * API-15-03: Xóa mềm addon service
     * Nếu đang được service_package active sử dụng → 409 SERVICE_003
     */
    @DeleteMapping("/{addonServiceId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<Void> deleteAddonService(
            @PathVariable Integer addonServiceId
    ) {

        addonServiceService.deleteAddonService(addonServiceId);

        return ApiResponse.success(
                "Addon service deleted successfully",
                null
        );
    }
}