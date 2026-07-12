package com.swp.autocarwash.refund.entity.enums;

/**
 * Chức năng: Vocabulary trạng thái của một yêu cầu hoàn tiền.
 * Cột {@code refund.status} lưu dạng String (theo convention repo).
 *
 * @author KimNgan
 * @version 1.0
 */
public enum RefundStatus {
    /** Đã tạo yêu cầu hoàn tiền, đang chờ Admin chuyển khoản. */
    PENDING,
    /** Admin đã chuyển khoản xong, có refundedAt/refundNote. */
    REFUNDED
}
