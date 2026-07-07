package com.swp.autocarwash.promotion.repository;

import com.swp.autocarwash.promotion.entity.PromotionStationMapping;
import com.swp.autocarwash.promotion.entity.PromotionStationMappingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionStationMappingRepository extends JpaRepository<PromotionStationMapping, PromotionStationMappingId> {

    List<PromotionStationMapping> findById_PromotionId(Integer promotionId);

}
