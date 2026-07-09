package com.swp.autocarwash.booking.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Chức năng: Event phát ra khi tiền cọc của booking được xác nhận (qua webhook
 * SePay hoặc admin xác nhận thủ công) — dùng cho các side-effect như gửi thông
 * báo cho khách.
 *
 * @author Ngân
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class BookingDepositPaidEvent {

    private final Long bookingId;

    private final Long customerId;

    private final BigDecimal depositAmount;

    /** BANK_TRANSFER (webhook SePay) hoặc MANUAL (xác nhận thủ công). */
    private final String paymentMethod;
}
