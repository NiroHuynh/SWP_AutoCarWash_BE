package com.swp.autocarwash.loyalty.repository.custom;

import com.swp.autocarwash.loyalty.entity.CustomerTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface CustomerTierRepository
        extends JpaRepository<CustomerTier, Integer> {

    Optional<CustomerTier> findByTierName(String tierName);

    Optional<CustomerTier> findTop1ByMinPointsGreaterThanOrderByMinPointsAsc(int currentPoints);

}
