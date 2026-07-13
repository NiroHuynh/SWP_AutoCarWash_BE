package com.swp.autocarwash.promotion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class PromotionStationMappingId implements Serializable {

    @Column(name = "promotion_id")
    private Integer promotionId;

    @Column(name = "station_id")
    private Integer stationId;
}
