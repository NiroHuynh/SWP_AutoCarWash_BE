package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.booking.entity.Booking;

public interface BookingInvoicePort {
    /**
     * Tạo invoice cho booking.
     *
     * Flow:
     * - Nhận thông tin booking.
     * - Tính tiền.
     * - Tạo booking_invoice.
     *
     */
    void createInvoice(
            Booking booking
    );
}
