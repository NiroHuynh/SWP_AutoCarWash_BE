package com.swp.autocarwash.loyalty.repository.custom;

import com.swp.autocarwash.loyalty.entity.LoyaltyPointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface LoyaltyPointTransactionRepository
        extends JpaRepository<LoyaltyPointTransaction, Long> {

    /**
     * Lay cac giao dich diem cua khach trong khoang [from, to), moi nhat len dau.
     * Dung range thay vi ham YEAR()/MONTH() de bam theo convention repo va tranh
     * van de portability tren cot Instant.
     */
    @Query("SELECT t FROM LoyaltyPointTransaction t " +
           "LEFT JOIN FETCH t.booking b LEFT JOIN FETCH b.servicePackage " +
           "WHERE t.customer.id = :cid AND t.createdAt >= :from AND t.createdAt < :to " +
           "ORDER BY t.createdAt DESC")
    List<LoyaltyPointTransaction> findByCustomerAndPeriod(@Param("cid") Long cid,
                                                          @Param("from") Instant from,
                                                          @Param("to") Instant to);
}
