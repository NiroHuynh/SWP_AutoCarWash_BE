package com.swp.autocarwash.promotion.repository;

import com.swp.autocarwash.promotion.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
}
