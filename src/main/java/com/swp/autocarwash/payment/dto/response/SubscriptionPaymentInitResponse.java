package com.swp.autocarwash.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Chức năng: Thông tin thanh toán trả lại cho module subscription/FE sau khi
 * khởi tạo mua gói — FE dùng để hiển thị QR chuyển khoản.
 *
 * @author Ngân
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPaymentInitResponse {

    private Long invoiceId;

    /** Tên gói — hiển thị trên màn thanh toán (AC01). */
    private String planName;

    /** Số ngày hiệu lực của gói — hiển thị khi xem lại thông tin gia hạn (RENEW AC02). */
    private Integer durationDays;

    /** Nội dung chuyển khoản để webhook map invoice, dạng "SUB{invoiceId}". */
    private String transferContent;

    /** Số tiền khách cần chuyển = giá gói. */
    private BigDecimal amount;

    private String invoiceStatus;

    /** Thời điểm QR hết hạn = createdAt + PENDING_PAYMENT_TIMEOUT_MINUTES (AC01/AC03). */
    private LocalDateTime expiresAt;

    /** URL ảnh QR VietQR đã điền sẵn số tài khoản/số tiền/nội dung, FE chỉ cần {@code <img src>}. */
    private String qrImageUrl;

    /** Số tài khoản ngân hàng nhận tiền — hiển thị dạng text dự phòng bên cạnh QR. */
    private String bankAccountNumber;

    /** Mã ngân hàng theo chuẩn SePay, ví dụ "MBBank", "Vietcombank". */
    private String bankCode;

    /** Tên chủ tài khoản ngân hàng nhận tiền. */
    private String bankAccountName;

    /** Tên đầy đủ khách hàng đăng ký gói. */
    private String customerName;

    /** Biển số xe được chọn để đăng ký gói. */
    private String vehicleLicensePlate;

    /** Ngày gói bắt đầu có hiệu lực. */
    private LocalDate startDate;

    /** Ngày gói hết hạn. */
    private LocalDate endDate;

    /** Số xe đã dùng trong nhóm gia đình — chỉ áp dụng cho gói FAMILY. */
    private Integer slotsUsed;

    /** Số xe tối đa gói FAMILY cho phép — chỉ áp dụng cho gói FAMILY. */
    private Integer maxSlots;

    /** Tên hạng khách hàng được kế thừa cho các thành viên — chỉ áp dụng cho gói FAMILY. */
    private String inheritedTierName;
}
