package com.swp.autocarwash.customer.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.dto.request.AddFamilyMemberRequest;
import com.swp.autocarwash.customer.dto.request.CreateFamilyGroupRequest;
import com.swp.autocarwash.customer.dto.request.SearchInvitedCustomerResponse;
import com.swp.autocarwash.customer.dto.response.CreateFamilyGroupResponse;
import com.swp.autocarwash.customer.dto.response.DissolveGroupResponse;
import com.swp.autocarwash.customer.dto.response.FamilyGroupDetailsResponse;
import com.swp.autocarwash.customer.dto.response.RemoveMemberResponse;
import com.swp.autocarwash.customer.service.family.FamilyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/my-group")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<FamilyGroupDetailsResponse>> getMyFamilyGroupDetails() {

        FamilyGroupDetailsResponse data = familyGroupService.getFamilyGroupDetails();

        String msg = (data != null)
                ? "Family group information loaded successfully!"
                : "You haven't joined any family groups yet.";

        return ResponseEntity.ok(ApiResponse.<FamilyGroupDetailsResponse>builder()
                .success(true)
                .message(msg)
                .data(data) // Trả về data object lồng nhau hoặc null
                .build());
    }

    @DeleteMapping("/members/{memberCustomerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<RemoveMemberResponse>> removeMemberFromGroup(
            @PathVariable Long memberCustomerId) {

        // Gọi service thực thi xóa cứng và kiểm duyệt nghiệp vụ
        RemoveMemberResponse responseData = familyGroupService.removeMember(memberCustomerId);

        return ResponseEntity.ok(ApiResponse.<RemoveMemberResponse>builder()
                .success(true)
                .message("The member has been successfully removed from the family group. The group limit has been freed.")
                .data(responseData)
                .build());
    }

    @DeleteMapping("/dissolve")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<DissolveGroupResponse>> dissolveFamilyGroup() {

        // Gọi Service thực thi chuỗi hành động dọn dẹp và giải tán nhóm hàng loạt
        DissolveGroupResponse responseData = familyGroupService.dissolveFamilyGroup();

        return ResponseEntity.ok(ApiResponse.<DissolveGroupResponse>builder()
                .success(true)
                .message("Family group disbanded successfully. Linked subscription plan cancelled.")
                .data(responseData)
                .build());
    }
}

