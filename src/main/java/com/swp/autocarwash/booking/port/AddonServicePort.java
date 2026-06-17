package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;

import java.util.List;

public interface AddonServicePort {
    List<AddonServiceContract> getAllAddons();

    /**
     * tổng duration addon
     */
    Integer getTotalDuration(List<Integer> addonIds);
}
