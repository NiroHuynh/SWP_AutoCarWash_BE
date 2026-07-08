package com.swp.autocarwash.payment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Chức năng: Thông tin 1 giao dịch thanh toán thành công hiển thị cho khách
 * hàng trong màn hình Payment History (FE-61C-US-01) — chỉ phơi ra field cần
 * thiết, không lộ chi tiết ngân hàng nội bộ (gateway, referenceCode...).
 *
 * @author Ngân
 * @version 1.0
 */
@Data
@Builder
public class PaymentHistoryResponse {

    private Long id;

    private BigDecimal amount;

    /** CASH / BANK_TRANSFER. */
    private String paymentMethod;

    /** DEPOSIT / FULL_PAYMENT / SUBSCRIPTION. */
    private String paymentType;

    private String transactionCode;

    private LocalDateTime paidAt;

    /** Booking gắn với giao dịch cọc — null nếu là giao dịch mua gói. */
    private Long bookingId;

    /** Hóa đơn subscription gắn với giao dịch mua gói — null nếu là cọc booking. */
    private Long subscriptionInvoiceId;
}
