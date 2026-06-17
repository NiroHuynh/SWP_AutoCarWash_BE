package com.swp.autocarwash.customer.adapter.mock;

import com.swp.autocarwash.booking.port.VehiclePort;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
}
