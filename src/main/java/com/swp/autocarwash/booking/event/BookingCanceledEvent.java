package com.swp.autocarwash.booking.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class BookingCanceledEvent {

    private final Long bookingId;
    private final Long vehicleId;
    private final Long customerId;
    private final Long canceledByStaffId;
    private final String bookingType;
    private final Boolean isDepositPaid;
    private final Instant checkInAt;
    private final Instant canceledAt;

}
