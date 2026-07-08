package com.swp.autocarwash.payment.repository;

import com.swp.autocarwash.payment.entity.PaymentWebhookLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentWebhookLogRepository extends JpaRepository<PaymentWebhookLog, Long> {

    boolean existsBySepayTransactionId(String sepayTransactionId);

    List<PaymentWebhookLog> findByStatusOrderByReceivedAtDesc(String status);
}
