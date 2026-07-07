package com.swp.autocarwash.payment.repository;

import com.swp.autocarwash.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository truy vấn dữ liệu giao dịch thanh toán ({@link Payment}).
 *
 * @author Ngọc
 * @version 1.0
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Lịch sử giao dịch thanh toán thành công của 1 khách hàng — cover cả cọc
     * booking ({@code bookingInvoice.booking.customer}) lẫn mua gói subscription
     * ({@code subscriptionInvoice.customer}), lọc thêm theo type/khoảng ngày nếu có truyền.
     */
    @Query("""
            SELECT p FROM Payment p
            LEFT JOIN p.bookingInvoice bi LEFT JOIN bi.booking b
            LEFT JOIN p.subscriptionInvoice si
            WHERE p.paymentStatus = 'SUCCESS'
              AND (b.customer.id = :customerId OR si.customer.id = :customerId)
              AND (:type IS NULL OR p.paymentType = :type)
              AND (:fromDate IS NULL OR p.paidAt >= :fromDate)
              AND (:toDate IS NULL OR p.paidAt <= :toDate)
            ORDER BY p.paidAt DESC
            """)
    List<Payment> findSuccessfulPaymentsByCustomerId(
            @Param("customerId") Long customerId,
            @Param("type") String type,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);
}
