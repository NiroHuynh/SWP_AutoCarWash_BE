package com.swp.autocarwash.customer.adapter;

import com.swp.autocarwash.booking.port.VehiclePort;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.customer.service.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Real adapter implementation of VehiclePort for production environment
 * Acts as bridge between external modules and VehicleService
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("pro")
@RequiredArgsConstructor
public class VehicleBookingAdapter implements VehiclePort {

    private final VehicleService vehicleService;

    /**
     * Get all vehicles of a customer
     */
    @Override
    public List<VehicleContract> getVehiclesByCustomer(Integer customerId) {
        return vehicleService.getVehiclesByCustomer(customerId);
    }

    /**
     * Get current logged-in customer id
     * (usually lấy từ SecurityContext)
     */
    @Override
    public Integer getCurrentCustomerId() {
        // TODO: integrate Spring Security later
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Validate vehicle ownership
     */
    @Override
    public boolean validateVehicleOwnership(Integer vehicleId, Integer customerId) {
        return vehicleService.validateVehicleOwnership(vehicleId, customerId);
    }

    /**
     * Get vehicle detail by id
     */
    @Override
    public VehicleContract getById(Integer id) {
        return vehicleService.getById(id);
    }
}
