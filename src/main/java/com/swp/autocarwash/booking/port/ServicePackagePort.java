package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;

import java.util.List;

public interface ServicePackagePort {
    /**
     * Lấy toàn bộ service package (KHÔNG filter station)
     */
    List<ServicePackageContract> getAllPackages();

    /**
     * lấy duration của service package
     */
    Integer getDuration(Integer servicePackageId);
}
