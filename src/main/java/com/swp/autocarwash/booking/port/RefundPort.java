package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.common.contract.refund.RefundContract;

import java.util.Optional;

/**
 * Chức năng: Cổng (hexagonal) để module booking đọc dữ liệu Refund gắn với 1 booking,
 * phục vụ hiển thị trên booking card/detail (khi status là REFUND_PENDING/REFUNDED).
 * Module refund cung cấp adapter implement cổng này.
 *
 * @author KimNgan
 * @version 1.0
 */
public interface RefundPort {

    /**
     * Lấy thông tin refund gắn với booking, nếu có.
     *
     * @param bookingId mã booking
     * @return snapshot refund, rỗng nếu booking chưa có yêu cầu hoàn tiền
     */
    Optional<RefundContract> findByBookingId(Long bookingId);
}
