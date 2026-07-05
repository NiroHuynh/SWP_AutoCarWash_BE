package com.swp.autocarwash.loyalty.repository;

import com.swp.autocarwash.loyalty.entity.LoyaltyPointTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoyaltyPointTransactionRepository
        extends JpaRepository<LoyaltyPointTransaction,Long> {

    List<LoyaltyPointTransaction> findTop5ByCustomer_IdOrderByCreatedAtDesc(Long customerId);

    Page<LoyaltyPointTransaction> findByCustomer_IdOrderByCreatedAtDesc(
            Long customerId,
            Pageable pageable
    );
}
