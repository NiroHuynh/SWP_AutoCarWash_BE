package com.swp.autocarwash.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveMemberResponse {
    
    private Long familyGroupId;
    private Long removedCustomerId;
    private String newUsageSummary; // Trả về tỷ lệ hạn mức mới, ví dụ "2/5"
}
