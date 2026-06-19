package com.swp.autocarwash.customer.service.vehicle;

import com.swp.autocarwash.common.contract.customer.VehicleContract;

import java.util.List;

/**
 *
 * Business service for Vehicle module
 *
 * @author Phong
 * @version 1.0
 */
public interface VehicleService {

    List<VehicleContract> getVehiclesByCustomer(Integer customerId);

    VehicleContract getById(Integer id);

    boolean validateVehicleOwnership(Integer vehicleId, Integer customerId);
}
