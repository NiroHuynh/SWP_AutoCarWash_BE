package com.swp.autocarwash.loyalty.entity;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.customer.entity.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loyalty_point_transaction", schema = "swp_auto_car_wash")
public class LoyaltyPointTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Size(max = 20)
    @NotNull
    @Column(name = "transaction_type", nullable = false, length = 20)
    private String transactionType;

    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "balance_after", nullable = false)
    private Integer balanceAfter;


}