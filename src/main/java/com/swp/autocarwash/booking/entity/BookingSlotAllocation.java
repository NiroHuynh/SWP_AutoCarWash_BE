package com.swp.autocarwash.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking_slot_allocation", schema = "swp_auto_car_wash")
public class BookingSlotAllocation {
    @EmbeddedId
    private BookingSlotAllocationId id;

    @MapsId("bookingId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @MapsId("bookingSlotId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_slot_id", nullable = false)
    private BookingSlot bookingSlot;


}