package com.swp.autocarwash.payment.repository;

import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
                                       @Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to);

    boolean existsByUnlimitSubscription_IdAndStatus(
            Long subscriptionId,
            String status
    );

    Optional<SubscriptionInvoice> findById(Long id);

    /**
     * Tim cac hoa don mua goi o mot trang thai va tao truoc moc thoi gian —
     * dung cho job tu huy hoa don PENDING qua han chuyen khoan.
     */
    java.util.List<SubscriptionInvoice> findByStatusAndCreatedAtBefore(String status, LocalDateTime cutoff);

    /**
     * Hoa don moi nhat cua mot goi unlimited theo trang thai — dung cho idempotent
     * gia han (tra lai invoice PENDING dang cho thay vi tao trung khi khach quay lai man QR).
     */
    java.util.Optional<SubscriptionInvoice> findFirstByUnlimitSubscription_IdAndStatusOrderByCreatedAtDesc(
            Long unlimitSubscriptionId, String status);

    /**
     * Hoa don moi nhat theo XE (qua goi unlimited) va trang thai — dung cho idempotent
     * dang ky moi: xe da co invoice PENDING dang cho thi tra lai QR do.
     */
    java.util.Optional<SubscriptionInvoice> findFirstByUnlimitSubscription_Vehicle_IdAndStatusOrderByCreatedAtDesc(
            Long vehicleId, String status);

    /**
     * Hoa don moi nhat cua mot goi family theo trang thai — dung cho idempotent
     * dang ky/gia han: nhom da co invoice PENDING dang cho thi tra lai QR do.
     */
    java.util.Optional<SubscriptionInvoice> findFirstByFamilySubscription_IdAndStatusOrderByCreatedAtDesc(
            Long familySubscriptionId, String status);

}
