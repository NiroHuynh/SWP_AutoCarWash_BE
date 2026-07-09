package com.swp.autocarwash.payment.entity.enums;

/**
 * Chức năng: Trạng thái giao dịch thanh toán — giá trị của cột
 * {@code payment.payment_status} (cột String, ghi bằng {@code .name()} theo
 * convention dự án).
 *
 * <ul>
 *   <li>{@code SUCCESS} — giao dịch thành công.</li>
 *   <li>{@code FAILED} — giao dịch thất bại.</li>
 * </ul>
 *
 * @author Ngân
 * @version 1.0
 */
public enum PaymentStatus {
    SUCCESS,
    FAILED
}
