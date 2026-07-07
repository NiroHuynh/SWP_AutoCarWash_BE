package com.swp.autocarwash.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Chức năng: Thông tin một webhook giao dịch SePay trả về cho admin đối soát.
 *
 * @author Ngân
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookLogResponse {

    private Long id;

    private String sepayTransactionId;

    private String gateway;

    private BigDecimal transferAmount;

    private String transferContent;

    private String transactionDate;

    private Long bookingId;

    private Long subscriptionInvoiceId;

    private String status;

    private String note;

    private LocalDateTime receivedAt;
}
