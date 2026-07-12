package com.swp.autocarwash.servicepackage.entity;

import com.swp.autocarwash.servicepackage.entity.enums.ServicePackageStatus;
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
@Table(name = "service_package", schema = "swp_auto_car_wash")
public class ServicePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_category_id", nullable = false)
    private ServiceCategory serviceCategory;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "required_slot", nullable = false)
    private Integer requiredSlot;

    @ColumnDefault("0.00")
    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @ColumnDefault("0")
    @Column(name = "total_reviews")
    private Integer totalReviews;

    @ColumnDefault("0")
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "package_addon_mapping",
            schema = "swp_auto_car_wash",
            joinColumns = @JoinColumn(name = "service_package_id"),
            inverseJoinColumns = @JoinColumn(name = "addon_service_id")
    )
    private List<AddonService> addonServices = new ArrayList<>();

}