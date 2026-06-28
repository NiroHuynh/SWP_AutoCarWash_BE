package com.swp.autocarwash.payment.repository;

import com.swp.autocarwash.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository truy vấn dữ liệu giao dịch thanh toán ({@link Payment}).
 *
 * @author Ngọc
 * @version 1.0
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
