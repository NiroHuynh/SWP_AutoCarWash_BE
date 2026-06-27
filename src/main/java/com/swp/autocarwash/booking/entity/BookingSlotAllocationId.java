package com.swp.autocarwash.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
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