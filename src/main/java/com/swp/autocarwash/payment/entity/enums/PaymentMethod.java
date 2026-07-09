package com.swp.autocarwash.payment.entity.enums;

/**
 * Chức năng: Phương thức thanh toán — giá trị của cột {@code payment.payment_method}
 * (cột String, ghi bằng {@code .name()} theo convention dự án).
 *
 * <ul>
 *   <li>{@code CASH} — tiền mặt tại quầy.</li>
 *   <li>{@code BANK_TRANSFER} — chuyển khoản ngân hàng, xác nhận qua webhook SePay.</li>
 *   <li>{@code MANUAL} — admin/staff xác nhận thủ công khi webhook lỗi.</li>
 *   <li>{@code MOMO}, {@code VNPAY} — ví điện tử (hiện chỉ có trong seed data, chưa có luồng code).</li>
 * </ul>
 *
 * @author Ngân
 * @version 1.0
 */
public enum PaymentMethod {
    CASH,
    BANK_TRANSFER,
    MANUAL,
    MOMO,
    VNPAY
}
