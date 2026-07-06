package com.swp.autocarwash.customer.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVehicleRequest {
    private String licensePlate;
    private String brandName;
    private String color;
}
