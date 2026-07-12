package com.swp.autocarwash.subscription.entity;

import com.swp.autocarwash.servicepackage.entity.ServiceCategory;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionPlanStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscription_plan", schema = "swp_auto_car_wash")
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_package_id")
    private ServicePackage servicePackage;

    @Size(max = 100)
    @NotNull
    @Column(name = "plan_name", nullable = false, length = 100)
    private String planName;

    @NotNull
    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @NotNull
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_category_id")
    private ServiceCategory serviceCategory;

    @Size(max = 20)
    @NotNull
    @Column(name = "plan_type", nullable = false, length = 20)
    private String planType;

    @Column(name = "max_vehicle_count")
    private Integer maxVehicleCount;

    @Lob
    @Column(name = "description")
    private String description;

    @ColumnDefault("0")
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private SubscriptionPlanStatus status = SubscriptionPlanStatus.ACTIVE;

}