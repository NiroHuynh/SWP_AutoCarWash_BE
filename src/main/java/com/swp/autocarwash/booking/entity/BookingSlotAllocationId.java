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
@EqualsAndHashCode  //để Java so sánh 2 cục Id xem có trùng nhau không
@Embeddable // Khoá nhúng
//Class này là cái bọc để nhúng vào Entity khác, nhúng cái bọc này làm Id(nó là Composite key)
//Class này ôm khít 2 cột của khoá đó -> bảng chính nhùng cái bọc này vào làm Id là xong
public class BookingSlotAllocationId implements Serializable {
    private static final long serialVersionUID = -1226075637485705296L;

    @NotNull
    @Column(name = "booking_id", nullable = false)
    private Long bookingId; //cột này lưu ID của booking dưới DB

    @NotNull
    @Column(name = "booking_slot_id", nullable = false)
    private Long bookingSlotId; // lưu id của booking slot dưới DB


}