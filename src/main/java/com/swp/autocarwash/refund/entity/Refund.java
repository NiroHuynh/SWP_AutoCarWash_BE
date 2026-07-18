package com.swp.autocarwash.refund.entity;

import com.swp.autocarwash.booking.entity.Booking;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Chức năng: Yêu cầu hoàn tiền cọc phát sinh khi khách hủy booking hợp lệ.
 * Quan hệ 1-1 với Booking (mỗi booking tối đa 1 refund).
 *
 * <p>Thông tin tài khoản nhận tiền lưu tại đây (không lưu ở Customer) vì mỗi lần hủy
 * có thể hoàn về STK khác nhau. {@code refundAmount} là snapshot cứng tại thời điểm hủy.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refund", schema = "swp_auto_car_wash")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** Booking gốc phát sinh yêu cầu hoàn tiền (UNIQUE: 1 booking chỉ 1 refund). */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    /** Hình thức hoàn cọc khách chọn (xem {@link com.swp.autocarwash.refund.entity.enums.RefundMethod}). */
    @Column(name = "refund_method", nullable = false, length = 20)
    private String refundMethod;

    /** Chỉ có giá trị khi {@code refundMethod = LOYALTY_POINTS} — số điểm đã cộng cho khách. */
    @Column(name = "points_awarded")
    private Integer pointsAwarded;

    /** Null khi hoàn cọc bằng điểm tích lũy (không cần thông tin ngân hàng). */
    @Column(name = "refund_bank_name", length = 100)
    private String refundBankName;

    /** BIN của ngân hàng nhận tiền — lưu để đối soát / build QR khi Admin chuyển. */
    @Column(name = "refund_bank_bin", length = 20)
    private String refundBankBin;

    @Column(name = "refund_account_number", length = 30)
    private String refundAccountNumber;

    @Column(name = "refund_account_holder", length = 100)
    private String refundAccountHolder;

    /**
     * Số tiền hoàn — snapshot cứng tại thời điểm tạo refund (copy từ
     * {@code RefundableBookingContract.depositAmount}, xem RefundServiceImpl#createRefund).
     * KHÔNG được tính lại động từ SystemSetting (DEFAULT_DEPOSIT_AMOUNT) khi đọc, vì:
     * - Setting có thể đổi sau khi refund đã tạo → phải giữ đúng số tiền đã cam kết hoàn.
     * - Cần cho đối soát/audit: biết chính xác đã/sẽ hoàn bao nhiêu cho từng refund.
     */
    @Column(name = "refund_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal refundAmount;

    /** Trạng thái refund (String, xem {@link com.swp.autocarwash.refund.entity.enums.RefundStatus}). */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** Mã giao dịch chuyển khoản (Admin nhập tay khi đã hoàn tiền) — phase Admin. */
    @Column(name = "refund_note", length = 255)
    private String refundNote;

    /** Thời điểm Admin xác nhận đã hoàn tiền xong — phase Admin. */
    @Column(name = "refunded_at")
    private Instant refundedAt;

    /** Admin (user.id) đã xử lý hoàn tiền — phase Admin. */
    @Column(name = "refunded_by")
    private Long refundedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
