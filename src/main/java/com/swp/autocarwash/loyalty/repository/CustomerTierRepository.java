package com.swp.autocarwash.loyalty.repository;

import com.swp.autocarwash.loyalty.entity.CustomerTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface CustomerTierRepository
        extends JpaRepository<CustomerTier, Integer> {

    Optional<CustomerTier> findByTierName(String tierName);

//    lấy pointMultiple của customer
    @Query("""
        SELECT ct.pointMultiple
        FROM Customer c
        JOIN c.customerTier ct
        WHERE c.id = :customerId
    """)
    BigDecimal findPointMultipleByCustomerId(
            @Param("customerId") Long customerId
    );

//    lấy tier tiếp theo tính bằng điểm đầu vào
    Optional<CustomerTier>
    findFirstByMinPointsLessThanEqualOrderByMinPointsDesc(
            Integer points
    );
}
