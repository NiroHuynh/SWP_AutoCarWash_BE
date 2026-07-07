package com.swp.autocarwash.promotion.repository;

import com.swp.autocarwash.promotion.entity.PromotionTarget;
import com.swp.autocarwash.promotion.entity.PromotionTargetMapping;
import com.swp.autocarwash.promotion.entity.PromotionTargetMappingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionTargetMappingRepository extends JpaRepository<PromotionTargetMapping, PromotionTargetMappingId> {
    
}
