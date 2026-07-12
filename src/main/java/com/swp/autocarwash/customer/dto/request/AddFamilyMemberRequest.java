package com.swp.autocarwash.customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddFamilyMemberRequest {
    private Long familyGroupId;
    private Long invitedCustomerId;
    private Long vehicleId;
}
