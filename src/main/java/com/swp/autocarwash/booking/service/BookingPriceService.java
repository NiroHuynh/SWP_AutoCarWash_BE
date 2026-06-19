package com.swp.autocarwash.booking.service;


import com.swp.autocarwash.booking.dto.request.BookingPricePreviewRequest;
import com.swp.autocarwash.booking.dto.response.BookingPricePreviewResponse;

public interface BookingPriceService {

    /**
     * Calculate preview price:
     * - service package price
     * - addon price
     * - voucher discount
     */
    BookingPricePreviewResponse calculatePreviewPrice(BookingPricePreviewRequest request);
}
