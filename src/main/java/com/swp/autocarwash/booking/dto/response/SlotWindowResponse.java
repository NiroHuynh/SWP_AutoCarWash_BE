package com.swp.autocarwash.booking.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class SlotWindowResponse {

    private List<Integer> slotIds;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean available;
}
