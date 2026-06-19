package com.swp.autocarwash.servicepackage.adapter.mock;

import com.swp.autocarwash.booking.port.AddonServicePort;
import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Profile("dev")
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

    @Override
    public BigDecimal calculateAddonPrice(List<Integer> addonIds) {
        if (addonIds == null) return BigDecimal.ZERO;
        return BigDecimal.valueOf(addonIds.size() * 20000);
    }

    @Override
    public List<AddonServiceContract> getByIds(List<Integer> ids) {
        return ids.stream().map(id -> {
            AddonServiceContract c = new AddonServiceContract();
            c.setId(id);
            c.setPrice(new BigDecimal("20000"));
            c.setDurationMinutes(15);
            return c;
        }).toList();
    }
}
