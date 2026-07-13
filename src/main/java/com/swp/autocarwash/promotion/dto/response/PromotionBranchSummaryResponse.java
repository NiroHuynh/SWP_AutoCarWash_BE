package com.swp.autocarwash.promotion.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PromotionBranchSummaryResponse {

    private Integer stationId;
    private String stationName;
    private Long totalActivePromotions;

}
