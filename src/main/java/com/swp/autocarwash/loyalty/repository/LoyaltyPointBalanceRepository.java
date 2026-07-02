package com.swp.autocarwash.loyalty.repository;

import com.swp.autocarwash.loyalty.entity.LoyaltyPointBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoyaltyPointBalanceRepository extends JpaRepository<LoyaltyPointBalance,Long> {
    Optional<LoyaltyPointBalance> findLoyaltyPointBalanceByCustomerId(Long customerId);

}
