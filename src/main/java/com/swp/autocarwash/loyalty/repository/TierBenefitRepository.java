package com.swp.autocarwash.loyalty.repository;

import com.swp.autocarwash.loyalty.entity.TierBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TierBenefitRepository extends JpaRepository<TierBenefit, Integer> {

    /** Danh sach quyen loi cua 1 hang thanh vien, theo thu tu id tang dan. */
    List<TierBenefit> findByCustomerTier_IdOrderByIdAsc(Integer customerTierId);
}
