package com.swp.autocarwash.customer.service.vehicle;

import com.swp.autocarwash.customer.dto.request.CreateVehicleRequest;
import com.swp.autocarwash.customer.dto.response.CreateVehicleResponse;
import org.springframework.transaction.annotation.Transactional;
import com.swp.autocarwash.common.contract.customer.VehicleContract;

import java.util.List;



/**
 *
 * Chức năng: VehicleService định nghĩa các nghiệp vụ xử lý liên quan đến vehicle.
 * Interface này cung cấp các chức năng lấy danh sách xe, lấy thông tin chi tiết
 * và kiểm tra quyền sở hữu vehicle phục vụ cho các module khác.
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
