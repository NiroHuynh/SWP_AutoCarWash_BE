package com.swp.autocarwash.loyalty.repository;

import com.swp.autocarwash.loyalty.entity.LoyaltyPointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyPointTransactionRepository
        extends JpaRepository<LoyaltyPointTransaction,Long> {
}
