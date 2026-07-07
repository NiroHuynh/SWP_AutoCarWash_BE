package com.swp.autocarwash.promotion.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "promotion_station_mapping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionStationMapping {

    @EmbeddedId
    private PromotionStationMappingId id;
}
