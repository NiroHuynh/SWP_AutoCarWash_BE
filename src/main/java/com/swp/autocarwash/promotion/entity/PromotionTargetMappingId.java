package com.swp.autocarwash.promotion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionTargetMappingId implements Serializable {
    private static final long serialVersionUID = 3732419156193724817L;
    @NotNull
    @Column(name = "promotion_id", nullable = false)
    private Integer promotionId;

    @NotNull
    @Column(name = "promotion_target_id", nullable = false)
    private Integer promotionTargetId;


}