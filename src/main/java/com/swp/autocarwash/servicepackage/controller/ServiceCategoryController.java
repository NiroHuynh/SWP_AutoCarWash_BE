package com.swp.autocarwash.servicepackage.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.servicepackage.dto.response.ServiceCategoryResponse;
import com.swp.autocarwash.servicepackage.service.ServiceCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceCategoryController {

    private final ServiceCategoryService serviceCategoryService;

    @GetMapping("/api/admin/service-categories")
    public ApiResponse<List<ServiceCategoryResponse>> getServiceCategories() {

        List<ServiceCategoryResponse> response = serviceCategoryService.getAll().stream()
                .map(c -> ServiceCategoryResponse.builder()
                        .id(c.getId())
                        .name(c.getCategoryName())
                        .build())
                .toList();

        return ApiResponse.success(
                "Service categories retrieved successfully.",
                response
        );
    }
}
