package com.swp.autocarwash.booking.service;

import com.swp.autocarwash.booking.dto.response.BookingContextResponse;

public interface BookingContextService {

    /**
     * Build toàn bộ dữ liệu cho màn Schedule step
     */
    BookingContextResponse getBookingContext(Integer stationId);
}
