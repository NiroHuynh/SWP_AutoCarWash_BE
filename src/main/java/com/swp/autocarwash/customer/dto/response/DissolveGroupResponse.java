package com.swp.autocarwash.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DissolveGroupResponse {
    private Long familyGroupId;
    private Long ownerCustomerId;
    private int totalMembersKicked; // Số lượng người bị out nhóm
    private String subscriptionStatus;
}
