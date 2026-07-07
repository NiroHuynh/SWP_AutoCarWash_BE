package com.swp.autocarwash.promotion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "voucher", schema = "swp_auto_car_wash")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Size(max = 50)
    @NotNull
    @Column(name = "voucher_code", nullable = false, length = 50)
    private String voucherCode;

    @Column(name = "max_discount_amount", precision = 12, scale = 2)
    private BigDecimal maxDiscountAmount;

    @ColumnDefault("0.00")
    @Column(name = "min_order_value", precision = 12, scale = 2)
    private BigDecimal minOrderValue;

    @NotNull
    @Column(name = "usage_limit", nullable = false)
    private Integer usageLimit;

    @ColumnDefault("0")
    @Column(name = "used_count")
    private Integer usedCount;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Size(max = 20)
    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @ColumnDefault("0")
    @Column(name = "reusable")
    private Boolean reusable;

    @Column(name = "discount_percentage")
    private Integer discountPercentage;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;


}