package com.swp.autocarwash.servicepackage.adapter.mock;

import com.swp.autocarwash.booking.port.AddonServicePort;
import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MockAddonServiceBookingAdapter implements AddonServicePort {

    /**
     * Mock addon services
     * Không phụ thuộc station
     */
    @Override
    public List<AddonServiceContract> getAllAddons() {
        return List.of(
                new AddonServiceContract(1, "Wax", new BigDecimal("20000"), 15),
                new AddonServiceContract(2, "Interior Cleaning", new BigDecimal("30000"), 20),
                new AddonServiceContract(3, "Engine Wash", new BigDecimal("50000"), 25)
        );
    }

    @Override
    public Integer getTotalDuration(List<Integer> addonIds) {
        return addonIds == null ? 0 : addonIds.size() * 15;
    }
}
