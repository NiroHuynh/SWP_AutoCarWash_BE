package com.swp.autocarwash.loyalty.repository.custom;

import com.swp.autocarwash.loyalty.entity.LoyaltyPointBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoyaltyPointBalanceRepository extends JpaRepository<LoyaltyPointBalance, Long> {

    /** Lay cac so du con diem (> nguong) - dung cho job reset thuong nien, tranh quet row = 0. */
    List<LoyaltyPointBalance> findByTotalPointsGreaterThan(Integer threshold);
}
