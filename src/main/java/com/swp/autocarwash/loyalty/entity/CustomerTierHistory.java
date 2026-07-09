package com.swp.autocarwash.loyalty.entity;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.loyalty.entity.enums.TierChangeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "customer_tier_history", schema = "swp_auto_car_wash")
public class CustomerTierHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /** Hang truoc do; null neu day la lan gan hang dau tien cua khach. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "old_tier_id")
    private CustomerTier oldTier;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "new_tier_id", nullable = false)
    private CustomerTier newTier;

    /**
     * Snapshot tai thoi diem chuyen hang - y nghia tuy nguon ghi: diem tich luy
     * (accumulatedPoints) neu tu flow checkout, hoac tong tien chi tieu (VND) neu
     * tu annual tier evaluation job (BR-FE-44).
     */
    @NotNull
    @Column(name = "value_at_transition", nullable = false)
    private Integer valueAtTransition;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false, length = 20)
    private TierChangeType changeType;

    /** Booking gay ra lan cong diem dan den chuyen hang nay (null neu khong xac dinh duoc). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
}
