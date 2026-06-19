package com.swp.autocarwash.booking.service;

import com.swp.autocarwash.booking.dto.request.BookingSlotRequest;
import com.swp.autocarwash.booking.dto.response.BookingSlotResponse;

public interface BookingSlotService {

    BookingSlotResponse getAvailableSlots(Integer stationId, BookingSlotRequest request);
}