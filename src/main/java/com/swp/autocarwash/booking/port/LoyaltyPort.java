package com.swp.autocarwash.booking.port;

public interface LoyaltyPort {
    Integer getLotaltyPoint(Long customerId);

    /** Tong diem cong (EARN) phat sinh tu booking, so duong. 0 neu khong co. */
    Integer getEarnedPointForBooking(Long bookingId);

    /** Tong diem da dung (REDEEM) cho booking, tra ve so duong. 0 neu khong co. */
    Integer getRedeemedPointForBooking(Long bookingId);
}
