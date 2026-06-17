package com.swp.autocarwash.common.contract.customer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleContract {
    private Integer id;
    private String licensePlate;
    private String brandName;
}
