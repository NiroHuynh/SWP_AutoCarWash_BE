package com.swp.autocarwash.customer.adapter.mock;

import com.swp.autocarwash.booking.port.VehiclePort;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
public class MockVehicleBookingAdapter implements VehiclePort {

    @Override
    public List<VehicleContract> getVehiclesByCustomer(Integer customerId) {
        return List.of(
                new VehicleContract(1, "71A-12345", "Honda City")
        );
    }

    @Override
    public Integer getCurrentCustomerId() {
        return 1;
    }

    @Override
    public boolean validateVehicleOwnership(Integer vehicleId, Integer customerId) {
        return true;
    }

    @Override
    public VehicleContract getById(Integer id) {
        VehicleContract v = new VehicleContract();
        v.setId(id);
        v.setCustomerId(1);
        return v;
    }
}
