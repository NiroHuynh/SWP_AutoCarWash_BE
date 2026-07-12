package com.swp.autocarwash.payment.entity.enums;

/**
 * Chức năng: Trạng thái xử lý của một webhook giao dịch nhận từ SePay.
 *
 * <ul>
 *   <li>{@code PROCESSED} — đã xác nhận cọc thành công cho booking.</li>
 *   <li>{@code ORPHAN} — không tìm được booking khớp nội dung chuyển khoản,
 *       chờ admin tra cứu xử lý thủ công.</li>
 *   <li>{@code AMOUNT_MISMATCH} — số tiền chuyển không khớp số tiền cọc yêu cầu.</li>
 *   <li>{@code INVALID_BOOKING_STATE} — booking tồn tại nhưng không ở trạng thái
 *       chờ cọc (đã CONFIRMED/CANCELED/đã đóng cọc...).</li>
 *   <li>{@code INVALID_INVOICE_STATE} — hóa đơn mua gói tồn tại nhưng không ở
 *       trạng thái PENDING (đã PAID/CANCEL).</li>
 * </ul>
 *
 * @author Ngân
 * @version 1.0
 */
public enum WebhookLogStatus {
    PROCESSED,
    ORPHAN,
    AMOUNT_MISMATCH,
    INVALID_BOOKING_STATE,
    INVALID_INVOICE_STATE
}
