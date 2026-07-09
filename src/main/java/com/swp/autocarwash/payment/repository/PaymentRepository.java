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

    /**
     * Toàn bộ giao dịch thanh toán thành công cho admin đối soát (FE-61C-US-02)
     * — không giới hạn theo customer, lọc thêm theo phương thức/trạng thái/loại/
     * khoảng ngày/Booking ID/Transaction ID/chi nhánh nếu có truyền.
     *
     * <p>Giao dịch subscription không gắn với station nào (SubscriptionInvoice
     * không có FK station) nên khi lọc theo {@code stationId} chỉ giao dịch
     * booking khớp đúng chi nhánh mới xuất hiện — FE hiển thị 2 tab riêng
     * (Subscription / Booking) để tránh nhầm lẫn thay vì gộp chung 1 filter.</p>
     *
     * <p>{@code SELECT DISTINCT} vì join qua {@code slotAllocations} (collection)
     * có thể nhân bản dòng nếu 1 booking có nhiều slot allocation.</p>
     */
    @Query("""
            SELECT DISTINCT p FROM Payment p
            LEFT JOIN p.bookingInvoice bi LEFT JOIN bi.booking b
            LEFT JOIN b.slotAllocations sa LEFT JOIN sa.bookingSlot bs LEFT JOIN bs.station st
            LEFT JOIN bi.customer bc LEFT JOIN bc.user bu
            LEFT JOIN p.subscriptionInvoice si LEFT JOIN si.customer sc LEFT JOIN sc.user su
            WHERE p.paymentStatus = 'SUCCESS'
              AND (:method IS NULL OR p.paymentMethod = :method)
              AND (:status IS NULL OR p.paymentStatus = :status)
              AND (:type IS NULL OR p.paymentType = :type)
              AND (:fromDate IS NULL OR p.paidAt >= :fromDate)
              AND (:toDate IS NULL OR p.paidAt <= :toDate)
              AND (:bookingId IS NULL OR b.id = :bookingId)
              AND (:transactionId IS NULL OR p.id = :transactionId)
              AND (:stationId IS NULL OR st.id = :stationId)
              AND (:phone IS NULL OR bu.phone LIKE CONCAT('%', :phone, '%') OR su.phone LIKE CONCAT('%', :phone, '%'))
            ORDER BY p.paidAt DESC
            """)
    List<Payment> findAllTransactions(
            @Param("method") String method,
            @Param("status") String status,
            @Param("type") String type,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("bookingId") Long bookingId,
            @Param("transactionId") Long transactionId,
            @Param("stationId") Integer stationId,
            @Param("phone") String phone);
}
