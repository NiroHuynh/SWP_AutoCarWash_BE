package com.swp.autocarwash.booking.entity;

import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.entity.enums.BookingType;
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

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "check_in_at")
    private Instant checkInAt;

    @Column(name = "check_out_at")
    private Instant checkOutAt;

    @Column(name = "canceled_at")
    private Instant canceledAt;

    @Builder.Default
    @ColumnDefault("0")
    @Column(name = "is_deposit_paid")
    private Boolean isDepositPaid = false;

//    @Column(name = "deposit_amount", precision = 12, scale = 2)
//    private BigDecimal depositAmount;

    @Builder.Default
    @ColumnDefault("0.00")
    @Column(name = "total_service_amount", precision = 12, scale = 2)
    private BigDecimal totalServiceAmount = BigDecimal.ZERO;

    @Builder.Default
    @ColumnDefault("0.00")
    @Column(name = "total_addon_amount", precision = 12, scale = 2)
    private BigDecimal totalAddonAmount = BigDecimal.ZERO;

    @Builder.Default
    @ColumnDefault("0.00")
    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Builder.Default
    @ColumnDefault("0.00")
    @Column(name = "voucher_discount_amount", precision = 12, scale = 2)
    private BigDecimal voucherDiscountAmount = BigDecimal.ZERO;

    @Builder.Default
    @ColumnDefault("0.00")
    @Column(name = "point_discount_amount", precision = 12, scale = 2)
    private BigDecimal pointDiscountAmount = BigDecimal.ZERO;

    /**
     * GIẢ ĐỊNH BỔ SUNG (không có trong schema gốc): thời điểm Scheduler (Subtask 4.1)
     * đã xử lý tịch thu cọc của booking này. Dùng để đảm bảo idempotent - không xử lý
     * lại nếu job vô tình chạy 2 lần trong cùng 1 ngày (lỗi/restart server).
     *
     * Số tiền cọc bị tịch thu KHÔNG lưu ở đây - vì mức cọc là 1 con số CỐ ĐỊNH chung
     * cho toàn hệ thống, được đọc trực tiếp từ bảng system_setting
     * (xem SystemSettingService.KEY_DEFAULT_DEPOSIT_AMOUNT) mỗi khi cần dùng,
     * không cần lưu thêm 1 bản sao trên Booking.
     */
    @Column(name = "deposit_confiscated_at")
    private LocalDateTime depositConfiscatedAt;

}