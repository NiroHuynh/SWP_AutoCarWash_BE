package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.common.contract.customer.VehicleContract;

import java.util.List;

public interface VehiclePort {
    /**
     * Lấy danh sách xe của customer hiện tại
     */
    List<VehicleContract> getVehiclesByCustomer(Integer customerId);
}
