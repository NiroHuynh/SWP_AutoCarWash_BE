package com.swp.autocarwash.customer.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.dto.request.CreateFamilyGroupRequest;
import com.swp.autocarwash.customer.dto.response.CreateFamilyGroupResponse;
import com.swp.autocarwash.customer.service.family.FamilyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/family-groups")
@RequiredArgsConstructor
public class FamilyGroupController {

    private final FamilyGroupService familyGroupService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateFamilyGroupResponse>> createFamilyGroup(
            @RequestBody CreateFamilyGroupRequest request) {

        CreateFamilyGroupResponse response = familyGroupService.createFamilyGroup(request);

        return ResponseEntity.ok(ApiResponse.<CreateFamilyGroupResponse>builder()
                .success(true)
                .message("Create family group successfully")
                .data(response)
                .build());
    }
}

