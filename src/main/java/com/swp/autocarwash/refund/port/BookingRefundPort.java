package com.swp.autocarwash.refund.port;

import com.swp.autocarwash.common.contract.booking.RefundableBookingContract;

/**
 * Chức năng: Cổng (hexagonal) để module refund lấy dữ liệu và tác động lên booking
 * mà không truy cập trực tiếp entity/repository của module booking.
 *
 * <p>Module booking cung cấp adapter {@code BookingRefundAdapter} implement cổng này.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
public interface BookingRefundPort {

    /**
     * Lấy snapshot booking phục vụ hoàn tiền, đồng thời kiểm tra quyền sở hữu.
     *
     * @param bookingId  mã booking
     * @param customerId id customer đang đăng nhập (từ JWT)
     * @return snapshot dữ liệu booking
     * @throws com.swp.autocarwash.common.exception.ResourceNotFoundException nếu không tìm thấy booking
     * @throws com.swp.autocarwash.common.exception.ForbiddenException nếu booking không thuộc customer
     */
    RefundableBookingContract loadRefundableBooking(Long bookingId, Long customerId);

    /**
     * Chuyển booking sang trạng thái REFUND_PENDING: set canceledAt, giải phóng slot đã đặt.
     *
     * @param bookingId mã booking
     */
    void markRefundPending(Long bookingId);

    /**
     * Chuyển booking sang trạng thái REFUNDED — Admin đã xác nhận chuyển khoản hoàn cọc xong (US-05 AC3).
     *
     * @param bookingId mã booking
     */
    void markRefunded(Long bookingId);

    /**
     * Hủy booking trực tiếp (CANCELED), không phát sinh Refund — dùng khi booking chưa
     * từng đặt cọc (vd lượt rửa subscription/family miễn phí) nên không có gì để hoàn.
     *
     * @param bookingId mã booking
     */
    void cancelWithoutRefund(Long bookingId);
}
