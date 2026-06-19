package com.swp.autocarwash.common.contract.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * Data transfer contract between modules (Vehicle Port)
 *
 * @author Phong
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleContract {
    private Integer id;
    private String licensePlate;
    private String brandName;
    private Integer customerId;

    public VehicleContract(Integer id, String licensePlate, String brandName) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.brandName = brandName;
    }
}
