package com.swp.autocarwash.refund.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Chức năng: Chi tiết 1 yêu cầu hoàn tiền cho Admin (US-05 AC2, AC2b, AC2c).
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundDetailResponse {

    private Long id;
    private Long bookingId;
    private String customerName;
    private String customerPhone;
    private String serviceCategoryName;
    private LocalDate appointmentDate;
    private String refundMethod;
    private BigDecimal refundAmount;
    private String refundBankName;
    private String refundAccountNumber;
    private String refundAccountHolder;
    /** Chỉ có giá trị khi refundMethod = LOYALTY_POINTS. */
    private Integer pointsAwarded;
    private String stationName;
    /** PENDING / REFUNDED. */
    private String status;
    /** Ảnh QR VietQR để Admin quét chuyển khoản — null nếu đã hoàn tiền hoặc bank không map được BIN (AC2c). */
    private String qrImageUrl;
    private String refundNote;
    private Instant refundedAt;
    private Long refundedBy;
    private Instant createdAt;
}
