package com.swp.autocarwash.payment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Chức năng: 1 dòng trong bảng Payment Transaction History cho admin
 * (FE-61C-US-02) — 7 field theo AC01: Transaction ID, Booking ID,
 * tên khách hàng, phương thức thanh toán, số tiền, trạng thái giao dịch, ngày tạo.
 *
 * @author Ngân
 * @version 1.0
 */
@Data
@Builder
public class PaymentTransactionResponse {

    /** Transaction ID. */
    private Long id;

    /** null nếu là giao dịch mua gói subscription (không gắn booking). */
    private Long bookingId;

    /** Tên khách hàng — lấy từ booking hoặc subscription invoice tùy loại giao dịch. */
    private String customerName;

    /** SĐT khách hàng — lấy từ booking hoặc subscription invoice tùy loại giao dịch. */
    private String customerPhone;

    private String paymentMethod;

    private BigDecimal amount;

    private String paymentStatus;

    private LocalDateTime paidAt;
}
