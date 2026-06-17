package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;

import java.math.BigDecimal;
import java.util.List;

public interface AddonServicePort {
    List<AddonServiceContract> getAllAddons();

    /**
     * tổng duration addon
     */
    Integer getTotalDuration(List<Integer> addonIds);

    /**
     * Calculate total addon price
     */
    BigDecimal calculateAddonPrice(List<Integer> addonIds);
}
