package com.swp.autocarwash.payment.entity;

import com.swp.autocarwash.booking.entity.Booking;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Chức năng: Lưu mọi webhook giao dịch ngân hàng nhận từ SePay — làm lịch sử
 * đối soát và làm khóa idempotency (mỗi sepay_transaction_id chỉ xử lý 1 lần).
 * Giao dịch không map được booking (ORPHAN) cũng được giữ lại tại đây để admin
 * tra cứu, không bỏ qua âm thầm.
 *
 * @author Ngân
 * @version 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_webhook_log", schema = "swp_auto_car_wash",
        uniqueConstraints = @UniqueConstraint(name = "uk_sepay_transaction_id", columnNames = "sepay_transaction_id"))
public class PaymentWebhookLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** Mã giao dịch phía SePay — khóa duy nhất chống xử lý trùng. */
    @Column(name = "sepay_transaction_id", nullable = false, length = 100)
    private String sepayTransactionId;

    @Column(name = "gateway", length = 100)
    private String gateway;

    @Column(name = "transfer_amount", precision = 12, scale = 2)
    private BigDecimal transferAmount;

    @Column(name = "transfer_content", length = 500)
    private String transferContent;

    @Column(name = "transaction_date", length = 50)
    private String transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    /** Hóa đơn mua gói liên quan (nullable — chỉ set với webhook nội dung SUB). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_invoice_id")
    private SubscriptionInvoice subscriptionInvoice;

    /** Giá trị của {@link com.swp.autocarwash.payment.entity.enums.WebhookLogStatus}. */
    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;
}
