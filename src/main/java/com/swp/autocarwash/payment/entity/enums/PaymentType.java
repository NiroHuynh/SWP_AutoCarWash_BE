package com.swp.autocarwash.payment.entity.enums;

/**
 * Chức năng: Loại giao dịch thanh toán — giá trị của cột {@code payment.payment_type}
 * (cột String, ghi bằng {@code .name()} theo convention dự án).
 *
 * <ul>
 *   <li>{@code DEPOSIT} — tiền cọc booking (20.000đ) qua chuyển khoản/manual.</li>
 *   <li>{@code FULL_PAYMENT} — thanh toán phần còn lại khi check-out.</li>
 *   <li>{@code SUBSCRIPTION} — thanh toán mua gói (SubscriptionInvoice).</li>
 *   <li>{@code REFUND} — hoàn tiền (gắn referencePayment tới giao dịch gốc).</li>
 * </ul>
 *
 * @author Ngân
 * @version 1.0
 */
public enum PaymentType {
    DEPOSIT,
    FULL_PAYMENT,
    SUBSCRIPTION,
    REFUND
}
