package com.swp.autocarwash.booking.event;

import java.math.BigDecimal;

public record BookingCompletedEvent(
    Long bookingId,
    Long customerId,
    BigDecimal totalAmount,
    Integer customerTierId,
    BigDecimal pointMultiple
){}
