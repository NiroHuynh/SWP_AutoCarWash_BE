package com.swp.autocarwash.payment.repository;

import com.swp.autocarwash.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository truy vấn dữ liệu giao dịch thanh toán ({@link Payment}).
 *
 * @author Ngọc
 * @version 1.0
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.bookingInvoice.id = :invoiceId ORDER BY p.paidAt DESC, p.id DESC")
    List<Payment> findByBookingInvoiceIdOrderByPaidAtDesc(@Param("invoiceId") Long invoiceId);
}
