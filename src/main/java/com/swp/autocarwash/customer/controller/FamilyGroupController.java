package com.swp.autocarwash.customer.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.dto.request.AddFamilyMemberRequest;
import com.swp.autocarwash.customer.dto.request.CreateFamilyGroupRequest;
import com.swp.autocarwash.customer.dto.request.SearchInvitedCustomerResponse;
import com.swp.autocarwash.customer.dto.response.CreateFamilyGroupResponse;
import com.swp.autocarwash.customer.service.family.FamilyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add-member")
    public ResponseEntity<ApiResponse<Void>> addFamilyMember(@RequestBody AddFamilyMemberRequest request) {

        familyGroupService.addFamilyMember(request);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Member added to the group successfully!")
                .build());
    }

    @GetMapping("/search-member")
    public ResponseEntity<ApiResponse<SearchInvitedCustomerResponse>> searchInvitedCustomer(
            @RequestParam("identifier") String identifier) {

        SearchInvitedCustomerResponse response = familyGroupService.searchInvitedCustomer(identifier);

        return ResponseEntity.ok(ApiResponse.<SearchInvitedCustomerResponse>builder()
                .success(true)
                .message("Found the customer information")
                .data(response)
                .build());
    }
}

