package com.swp.autocarwash.promotion.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "promotion_target_mapping", schema = "swp_auto_car_wash")
public class PromotionTargetMapping {
    @EmbeddedId
    private PromotionTargetMappingId id;

    @MapsId("promotionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @MapsId("promotionTargetId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promotion_target_id", nullable = false)
    private PromotionTarget promotionTarget;


}