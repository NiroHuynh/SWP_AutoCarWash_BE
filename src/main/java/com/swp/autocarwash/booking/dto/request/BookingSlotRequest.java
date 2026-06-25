package com.swp.autocarwash.booking.dto.request;


import lombok.Data;

import java.util.List;

@Data
public class BookingSlotRequest {

    private Integer servicePackageId;

    private List<Integer> addonServiceIds;

    private String appointmentDate;
}

