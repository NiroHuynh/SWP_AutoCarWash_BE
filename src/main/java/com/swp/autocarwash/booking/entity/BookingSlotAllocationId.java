package com.swp.autocarwash.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class BookingSlotAllocationId implements Serializable {
    private static final long serialVersionUID = -1226075637485705296L;
    @NotNull
    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @NotNull
    @Column(name = "booking_slot_id", nullable = false)
    private Long bookingSlotId;


}