package com.swp.autocarwash.booking.entity;

import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import com.swp.autocarwash.staff.entity.Staff;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking", schema = "swp_auto_car_wash")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_package_id", nullable = false)
    private ServicePackage servicePackage;

    @NotNull
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Size(max = 30)
    @NotNull
    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_in_employee_id")
    private Staff checkInEmployee;

    @Size(max = 20)
    @NotNull
    @Column(name = "booking_type", nullable = false, length = 20)
    private String bookingType;

    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "check_in_at")
    private LocalDateTime checkInAt;

    @Column(name = "check_out_at")
    private LocalDateTime checkOutAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @ColumnDefault("0")
    @Column(name = "is_deposit_paid")
    private Boolean isDepositPaid;

    @ColumnDefault("0.00")
    @Column(name = "total_service_amount", precision = 12, scale = 2)
    private BigDecimal totalServiceAmount;

    @ColumnDefault("0.00")
    @Column(name = "total_addon_amount", precision = 12, scale = 2)
    private BigDecimal totalAddonAmount;

    @ColumnDefault("0.00")
    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @ColumnDefault("0.00")
    @Column(name = "voucher_discount_amount", precision = 12, scale = 2)
    private BigDecimal voucherDiscountAmount;

    @ColumnDefault("0.00")
    @Column(name = "point_discount_amount", precision = 12, scale = 2)
    private BigDecimal pointDiscountAmount;

    @OneToMany(
            mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<BookingAddon> addons = new ArrayList<>();

    @OneToMany(
            mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<BookingSlotAllocation> slotAllocations
            = new ArrayList<>();
}