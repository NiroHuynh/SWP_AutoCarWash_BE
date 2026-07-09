package com.swp.autocarwash.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

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

    /** Nội dung chuyển khoản để webhook map invoice, dạng "SUB{invoiceId}". */
    private String transferContent;

    /** Số tiền khách cần chuyển = giá gói. */
    private BigDecimal amount;

    private String invoiceStatus;

    /** Thời điểm QR hết hạn = createdAt + PENDING_PAYMENT_TIMEOUT_MINUTES (AC01/AC03). */
    private Instant expiresAt;

    /** URL ảnh QR VietQR đã điền sẵn số tài khoản/số tiền/nội dung, FE chỉ cần {@code <img src>}. */
    private String qrImageUrl;
}
