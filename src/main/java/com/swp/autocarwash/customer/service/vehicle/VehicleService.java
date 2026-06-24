package com.swp.autocarwash.customer.service.vehicle;

import com.swp.autocarwash.customer.dto.request.CreateVehicleRequest;
import com.swp.autocarwash.customer.dto.response.CreateVehicleResponse;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * Service xử lý nghiệp vụ Vehicle
 *
 * @author Phong
 * @version 1.0
 */
public interface VehicleService {



    /**
     *
     * Thêm phương tiện mới cho customer
     *
     * @param request thông tin vehicle
     * @return vehicle response
     */
    CreateVehicleResponse createVehicle(
            Long userId,
            CreateVehicleRequest request
    );

}
