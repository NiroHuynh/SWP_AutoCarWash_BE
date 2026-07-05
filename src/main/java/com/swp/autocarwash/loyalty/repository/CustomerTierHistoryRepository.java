package com.swp.autocarwash.loyalty.repository;

import com.swp.autocarwash.loyalty.entity.CustomerTierHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CustomerTierHistoryRepository extends JpaRepository<CustomerTierHistory, Long> {

    /**
     * Lich su chuyen hang cua 1 khach trong khoang [from, to), moi nhat len dau,
     * fetch san oldTier/newTier de tranh N+1.
     */
    @Query("SELECT h FROM CustomerTierHistory h " +
           "LEFT JOIN FETCH h.oldTier LEFT JOIN FETCH h.newTier LEFT JOIN FETCH h.booking " +
           "WHERE h.customer.id = :cid AND h.createdAt >= :from AND h.createdAt < :to " +
           "ORDER BY h.createdAt DESC")
    List<CustomerTierHistory> findByCustomerAndPeriod(@Param("cid") Long cid,
                                                       @Param("from") Instant from,
                                                       @Param("to") Instant to);
}
