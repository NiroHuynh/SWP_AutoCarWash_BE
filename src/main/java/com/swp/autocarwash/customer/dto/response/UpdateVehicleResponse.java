package com.swp.autocarwash.customer.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
public class UpdateVehicleResponse {
    private Long id;
    private String licensePlate;
    private String brandName;
    private String color;
}
