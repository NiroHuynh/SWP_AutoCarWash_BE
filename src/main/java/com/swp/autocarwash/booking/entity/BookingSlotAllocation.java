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

    @EmbeddedId //chỉ định thằng này là khoá chính của bảng, nhúng từ cái bọc class Id hồi nãy
    private BookingSlotAllocationId id;

    @MapsId("bookingId")    //hibernate nó lấy bookingId của đối tượng Booking ở dưới gán đè vào
                            // bookingid nằm trong cái bọc ở trên
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @MapsId("bookingSlotId")  //tương tự
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_slot_id", nullable = false)
    private BookingSlot bookingSlot;


}