package com.swp.autocarwash.customer.service.family;

import com.swp.autocarwash.customer.dto.request.AddFamilyMemberRequest;
import com.swp.autocarwash.customer.dto.request.CreateFamilyGroupRequest;
import com.swp.autocarwash.customer.dto.request.SearchInvitedCustomerResponse;
import com.swp.autocarwash.customer.dto.response.CreateFamilyGroupResponse;
import com.swp.autocarwash.customer.dto.response.FamilyGroupDetailsResponse;

public interface FamilyGroupService {
    Long getOwnerCustomerIdOfCustomerId(Long customerId);

    CreateFamilyGroupResponse createFamilyGroup(CreateFamilyGroupRequest request);

    void addFamilyMember(AddFamilyMemberRequest request);

    SearchInvitedCustomerResponse searchInvitedCustomer(String identifier);

    FamilyGroupDetailsResponse getFamilyGroupDetails();
}
