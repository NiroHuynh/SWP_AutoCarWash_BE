package com.swp.autocarwash.payment.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Chức năng: Event phát ra khi hóa đơn mua gói (SubscriptionInvoice) được
 * thanh toán thành công (webhook SePay hoặc xác nhận thủ công). Module
 * subscription lắng nghe event này để kích hoạt gói (ACTIVE, tính endDate).
 *
 * @author Ngân
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class SubscriptionInvoicePaidEvent {

    private final Long invoiceId;

    private final Long customerId;

    private final BigDecimal planPrice;

    /** Id UnlimitSubscription gắn với invoice (nullable). */
    private final Long unlimitSubscriptionId;

    /** Id FamilySubscription gắn với invoice (nullable). */
    private final Long familySubscriptionId;

    /** BANK_TRANSFER (webhook SePay) hoặc MANUAL (xác nhận thủ công). */
    private final String paymentMethod;
}
