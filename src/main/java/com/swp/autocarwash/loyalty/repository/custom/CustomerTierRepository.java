package com.swp.autocarwash.loyalty.repository.custom;

import com.swp.autocarwash.loyalty.entity.CustomerTier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerTierRepository
        extends JpaRepository<CustomerTier, Integer> {

    Optional<CustomerTier> findByTierName(String tierName);

}
