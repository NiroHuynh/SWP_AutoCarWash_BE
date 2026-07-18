package com.swp.autocarwash.refund.entity.enums;

/**
 * Chức năng: Hình thức hoàn cọc mà khách hàng chọn khi hủy booking.
 *
 * @author KimNgan
 * @version 1.0
 */
public enum RefundMethod {
    /** Hoàn tiền về tài khoản ngân hàng — Admin xác nhận chuyển khoản thủ công. */
    BANK_TRANSFER,
    /** Chuyển cọc thành điểm tích lũy — cộng điểm ngay, không cần Admin duyệt. */
    LOYALTY_POINTS
}
