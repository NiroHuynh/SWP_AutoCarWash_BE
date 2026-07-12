package com.swp.autocarwash.refund.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Chức năng: 1 dòng trong danh sách hoàn tiền của Admin (US-05 AC1).
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundListItemResponse {

    private Long id;
    private Long bookingId;
    private String customerName;
    private String customerPhone;
    private String stationName;
    private BigDecimal refundAmount;
    /** PENDING / REFUNDED — FE map sang "Chờ xử lý" / "Đã hoàn tiền". */
    private String status;
    private Instant createdAt;
    private Instant refundedAt;
}
