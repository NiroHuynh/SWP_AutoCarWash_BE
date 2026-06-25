package com.swp.autocarwash.payment.entity;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.customer.entity.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking_invoice", schema = "swp_auto_car_wash")
public class BookingInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull
    @Column(name = "raw_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal rawAmount;

    @ColumnDefault("0.00")
    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @NotNull
    @Column(name = "final_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal finalAmount;

    @Size(max = 20)
    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("0.00")
    @Column(name = "voucher_discount", precision = 12, scale = 2)
    private BigDecimal voucherDiscount;

    @ColumnDefault("0.00")
    @Column(name = "point_discount", precision = 12, scale = 2)
    private BigDecimal pointDiscount;

    @NotNull
    @Column(name = "service_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal serviceAmount;

    @ColumnDefault("0.00")
    @Column(name = "addon_amount", precision = 12, scale = 2)
    private BigDecimal addonAmount;

    @Column(name = "paid_at")
    private Instant paidAt;


}