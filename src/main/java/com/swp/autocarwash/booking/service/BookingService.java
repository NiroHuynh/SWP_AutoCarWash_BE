package com.swp.autocarwash.booking.service;

import com.swp.autocarwash.booking.dto.request.CreateBookingRequest;
import com.swp.autocarwash.booking.dto.response.CreateBookingResponse;

import java.math.BigDecimal;

public interface BookingService {

    /**
     * Tạo booking mới + validate slot + voucher + price
     */
    CreateBookingResponse createBooking(CreateBookingRequest request);
}