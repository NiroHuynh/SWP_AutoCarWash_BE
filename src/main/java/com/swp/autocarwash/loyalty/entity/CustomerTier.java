package com.swp.autocarwash.loyalty.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_tier", schema = "swp_auto_car_wash")
public class CustomerTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "tier_name", nullable = false, length = 50)
    private String tierName;

    @NotNull
    @Column(name = "min_points", nullable = false)
    private Integer minPoints;

    @NotNull
    @Column(name = "booking_window_days", nullable = false)
    private Integer bookingWindowDays;

    @Column(name = "point_multiple", precision = 10, scale = 2)
    private BigDecimal pointMultiple;

    @ColumnDefault("0.00")
    @Column(name = "retention_target_amount", precision = 12, scale = 2)
    private BigDecimal retentionTargetAmount;

    @Builder.Default
    @ColumnDefault("0")
    @Column(name = "queue_priority_weight")
    private Integer queuePriorityWeight = 0;

    @OneToMany(mappedBy = "customerTier", fetch = FetchType.LAZY)
    private List<TierBenefit> tierBenefits = new ArrayList<>();

}