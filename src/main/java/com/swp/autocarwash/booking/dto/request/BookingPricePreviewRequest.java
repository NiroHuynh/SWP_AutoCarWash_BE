package com.swp.autocarwash.booking.dto.request;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingPricePreviewRequest {

    private Integer stationId;
    private Integer servicePackageId;
    private Long vehicleId;
    private List<Integer> addonServiceIds;
    private String appointmentDate;
    private String voucherCode;
}
