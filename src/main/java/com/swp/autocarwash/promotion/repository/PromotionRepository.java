package com.swp.autocarwash.promotion.repository;

import com.swp.autocarwash.promotion.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    Optional<Promotion> findByIdAndIsDeletedFalse(Integer id);
}
