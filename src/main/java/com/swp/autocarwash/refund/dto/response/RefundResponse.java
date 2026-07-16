package com.swp.autocarwash.refund.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Chức năng: Kết quả trả về sau khi tạo yêu cầu hoàn tiền.
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponse {

    private Long id;
    private Long bookingId;
    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private BigDecimal refundAmount;
    private String status;
    private String bookingStatus;
    private Instant createdAt;
}
