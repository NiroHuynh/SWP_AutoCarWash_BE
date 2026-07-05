package com.swp.autocarwash.loyalty.repository;

import com.swp.autocarwash.loyalty.entity.LoyaltyPointBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoyaltyPointBalanceRepository extends JpaRepository<LoyaltyPointBalance,Long> {
    Optional<LoyaltyPointBalance> findLoyaltyPointBalanceByCustomerId(Long customerId);

    /** Lay cac so du con diem (> nguong) - dung cho job reset thuong nien, tranh quet row = 0. */
    List<LoyaltyPointBalance> findByTotalPointsGreaterThan(Integer threshold);
}
