package com.swp.autocarwash.payment.repository;

import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface SubscriptionInvoiceRepository extends JpaRepository<SubscriptionInvoice, Long> {

    /**
     * Tong tien khach da thanh toan qua hoa don subscription trong khoang [from, to).
     * Loc theo paidAt (hoa don chua thanh toan co paidAt = NULL nen tu loai).
     */
    @Query("SELECT COALESCE(SUM(si.planPrice), 0) FROM SubscriptionInvoice si " +
           "WHERE si.customer.id = :cid AND si.paidAt >= :from AND si.paidAt < :to")
    BigDecimal sumPlanPricePaidBetween(@Param("cid") Long cid,
                                       @Param("from") Instant from,
                                       @Param("to") Instant to);

    boolean existsByUnlimitSubscription_IdAndStatus(
            Long subscriptionId,
            String status
    );

    Optional<SubscriptionInvoice> findById(Long id);
}
