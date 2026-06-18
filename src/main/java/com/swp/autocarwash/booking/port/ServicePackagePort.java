package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;

import java.math.BigDecimal;
import java.util.List;

public interface ServicePackagePort {
    /**
     * Lấy toàn bộ service package (KHÔNG filter station)
     */
    List<ServicePackageContract> getAllPackages();

    /**
     * lấy duration (dạng phút) của service package
     */
    Integer getDuration(Integer servicePackageId);

    /**
     * Get service package info by id
     */
    ServicePackageContract getServicePackage(Integer id);

    /**
     * Lấy thông tin package (giá + required slot)
     */
    ServicePackageContract getById(Integer id);
}
