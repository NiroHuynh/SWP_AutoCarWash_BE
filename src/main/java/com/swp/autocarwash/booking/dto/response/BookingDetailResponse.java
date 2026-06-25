package com.swp.autocarwash.booking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO phản hồi chi tiết đầy đủ của một lịch đặt xe.
 *
 * <p>Trả về toàn bộ thông tin cần thiết để render màn hình
 * "Booking Detail" theo AC-25.3.2.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Getter
@Builder
public class BookingDetailResponse {

    // ── Thông tin booking ─────────────────────────────────────────────────────

    /** Mã định danh của lịch đặt. */
    private Long bookingId;

    /** Trạng thái nội bộ (CONFIRMED, CHECKED_IN, WASHING, PAID, CANCELLED, NO_SHOW). */
    private String status;

    /** Loại booking (ONLINE, WALK_IN). */
    private String bookingType;

    /** Hạng thành viên của khách (PLATINUM/GOLD/SILVER...). {@code null} nếu booking không gắn customer (walk-in). */
    private String customerTier;

    // ── Thông tin dịch vụ ────────────────────────────────────────────────────

    /** Tên gói dịch vụ chính. */
    private String serviceName;

    /** Danh sách dịch vụ bổ sung đã chọn. Rỗng nếu không có addon. */
    private List<AddonInfo> addons;

    // ── Thông tin xe ─────────────────────────────────────────────────────────

    /** Biển số xe. */
    private String licensePlate;

    /** Hãng xe. */
    private String brandName;

    /** Màu sắc xe. */
    private String color;

    // ── Thông tin chi nhánh ──────────────────────────────────────────────────

    /** Tên chi nhánh phục vụ. */
    private String stationName;

    /** Địa chỉ chi nhánh. */
    private String stationAddress;

    // ── Lịch hẹn ─────────────────────────────────────────────────────────────

    /** Ngày hẹn rửa xe. */
    private LocalDate appointmentDate;

    /** Giờ bắt đầu dự kiến (slot đầu tiên). */
    private LocalTime startTime;

    /** Giờ kết thúc dự kiến (slot cuối cùng). */
    private LocalTime endTime;

    // ── Kỹ thuật viên ────────────────────────────────────────────────────────

    /**
     * Họ tên kỹ thuật viên phụ trách (firstName + lastName).
     * {@code null} nếu chưa được assign.
     */
    private String technicianName;

    // ── Thanh toán ────────────────────────────────────────────────────────────

    /** Giá dịch vụ chính. */
    private BigDecimal servicePrice;

    /** Tổng giá các addon. */
    private BigDecimal addonTotal;


    private BigDecimal depositAmount;
    /**
     * Mã voucher đã áp dụng.
     * {@code null} nếu không dùng voucher.
     */
    private String voucherCode;

    /**
     * Phần trăm giảm giá của voucher.
     * {@code null} nếu không dùng voucher hoặc voucher là flat discount.
     */
    private Integer voucherDiscountPercent;

    /** Số tiền được giảm bởi voucher. {@code 0} nếu không dùng voucher. */
    private BigDecimal voucherDiscountAmount;

    /** Tổng tiền sau khi áp dụng tất cả giảm giá. */
    private BigDecimal totalAmount;

    /** Trạng thái đã đặt cọc hay chưa. */
    private Boolean isDepositPaid;

    /** Số tiền còn lại cần thanh toán tại quầy (totalAmount - tiền cọc đã thanh toán). */
    private BigDecimal remainingAmount;
}
