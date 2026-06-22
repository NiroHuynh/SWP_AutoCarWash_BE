package com.swp.autocarwash.booking.dto.request;


import java.util.List;
import lombok.Data;

@Data
public class CreateBookingRequest {

    private Integer stationId;
    private Long vehicleId;
    private Integer servicePackageId;
    private List<Integer> addonServiceIds;
    private String appointmentDate;
    private List<Integer> slotIds;
    private String voucherCode;
}
