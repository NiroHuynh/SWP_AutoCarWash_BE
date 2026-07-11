package com.swp.autocarwash.servicepackage.controller;

import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.servicepackage.dto.response.AddonServiceResponse;
import com.swp.autocarwash.servicepackage.service.AddonServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Chức năng: Danh sách add-on service cho admin chọn khi tạo/sửa subscription plan
 * (add-on đi kèm gói - khác service_package_id quyết định hạng dịch vụ rửa xe).
 */
@RestController
@RequiredArgsConstructor
public class AddonServiceController {

    private final AddonServiceService addonServiceService;

    @GetMapping("/api/admin/addon-services/active")
    public ApiResponse<List<AddonServiceResponse>> getActiveAddonServices() {

        List<AddonServiceResponse> response = addonServiceService.getAll().stream()
                .map(this::toResponse)
                .toList();

        return ApiResponse.success(
                "Active addon services retrieved successfully.",
                response
        );
    }

    private AddonServiceResponse toResponse(AddonServiceContract contract) {
        return AddonServiceResponse.builder()
                .id(contract.getId())
                .name(contract.getName())
                .price(contract.getPrice())
                .durationMinutes(contract.getDurationMinutes())
                .build();
    }
}
