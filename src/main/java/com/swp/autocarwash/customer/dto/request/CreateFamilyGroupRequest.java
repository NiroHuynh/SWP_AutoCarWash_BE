package com.swp.autocarwash.customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFamilyGroupRequest {
    private String groupName;
    private Long vehicleId;

}
