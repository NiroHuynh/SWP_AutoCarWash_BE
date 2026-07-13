package com.swp.autocarwash.booking.entity.enums;

public enum BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELED,
    CHECK_IN,
    NO_SHOW,
    WASHING,
    COMPLETED,
    CHECK_OUT,
    /** Booking đã hủy hợp lệ, đang chờ Admin chuyển khoản hoàn cọc (có bản ghi Refund). */
    REFUND_PENDING,
    /** Đã hoàn tiền cọc xong (Refund.refundedAt khác NULL). */
    REFUNDED
}