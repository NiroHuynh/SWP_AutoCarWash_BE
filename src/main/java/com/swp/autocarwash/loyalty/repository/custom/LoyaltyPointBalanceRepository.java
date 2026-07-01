package com.swp.autocarwash.loyalty.repository.custom;

import com.swp.autocarwash.loyalty.entity.LoyaltyPointBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyPointBalanceRepository extends JpaRepository<LoyaltyPointBalance, Long> {
}
