package com.swp.autocarwash.promotion.dto.response;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionTargetResponse {
    private Integer id;
    private String targetName;
    private String targetCode;
    private String description;
}
