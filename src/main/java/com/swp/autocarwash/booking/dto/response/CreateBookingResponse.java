package com.swp.autocarwash.booking.dto.response;


import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingResponse {

    private Long bookingId;
    private String status;
    private BigDecimal totalAmount;
    private List<Integer> slotIds;
}