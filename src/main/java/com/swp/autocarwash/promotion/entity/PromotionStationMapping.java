package com.swp.autocarwash.promotion.entity;

import com.swp.autocarwash.station.entity.Station;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("promotionId") //Khớp chính xác với tên thuộc tính "promotionId" trong class PromotionStationMappingId
    @JoinColumn(name = "promotion_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("stationId") //Khớp chính xác với tên thuộc tính "stationId" trong class PromotionStationMappingId
    @JoinColumn(name = "station_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Station station;
}
