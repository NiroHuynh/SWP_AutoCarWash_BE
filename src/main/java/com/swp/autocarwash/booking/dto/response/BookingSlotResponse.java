package com.swp.autocarwash.booking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class BookingSlotResponse {

    private List<SlotWindowResponse> slots;
}