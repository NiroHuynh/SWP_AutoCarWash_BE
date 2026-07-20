package com.swp.autocarwash.system.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TierStatResponse {

    private String tier;

    private Long customerCount;

}
