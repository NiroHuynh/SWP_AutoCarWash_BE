package com.swp.autocarwash.payment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "payment", schema = "swp_auto_car_wash")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_invoice_id")
    private BookingInvoice bookingInvoice;

    @Size(max = 30)
    @NotNull
    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Size(max = 100)
    @Column(name = "transaction_code", length = 100)
    private String transactionCode;

    @Size(max = 20)
    @NotNull
    @Column(name = "payment_status", nullable = false, length = 20)
    private String paymentStatus;

    @CreationTimestamp
    @Column(name = "paid_at")
    private Instant paidAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_invoice_id")
    private SubscriptionInvoice subscriptionInvoice;

    @Column(name = "received_amount", precision = 12, scale = 2)
    private BigDecimal receivedAmount;

    @Size(max = 20)
    @NotNull
    @Column(name = "payment_type", nullable = false, length = 20)
    private String paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_payment_id")
    private Payment referencePayment;


}