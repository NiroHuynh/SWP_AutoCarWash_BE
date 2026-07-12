package com.swp.autocarwash.promotion.repository;

import com.swp.autocarwash.promotion.entity.PromotionTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionTargetRepository extends JpaRepository<PromotionTarget, Integer> {
}
