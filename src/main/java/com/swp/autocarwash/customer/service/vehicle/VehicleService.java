package com.swp.autocarwash.customer.service.vehicle;

import com.swp.autocarwash.customer.dto.request.CreateVehicleRequest;
import com.swp.autocarwash.customer.dto.request.UpdateVehicleRequest;
import com.swp.autocarwash.customer.dto.response.CreateVehicleResponse;
import com.swp.autocarwash.customer.dto.response.UpdateVehicleResponse;
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
     * Chức năng: Lấy danh sách vehicle thuộc về một customer.
     *
     * Quy trình:
     * - Nhận customerId cần tìm vehicle.
     * - Truy vấn danh sách vehicle từ database.
     * - Mapping dữ liệu sang VehicleContract.
     * - Trả về danh sách vehicle của customer.
     *
     * @param customerId id của customer cần lấy danh sách vehicle
     *
     * @return danh sách VehicleContract thuộc customer
     *
     * @author Phong
     * @version 1.0
     */
    List<VehicleContract> getVehiclesByCustomer(Long customerId);

    /**
     *
     * Chức năng: Lấy thông tin chi tiết vehicle theo vehicle id.
     *
     * Quy trình:
     * - Nhận vehicleId cần tìm.
     * - Truy vấn vehicle trong database.
     * - Mapping entity sang VehicleContract.
     * - Trả về thông tin vehicle.
     *
     * @param id id của vehicle cần lấy thông tin
     *
     * @return VehicleContract chứa thông tin vehicle
     *
     * @author Phong
     * @version 1.0
     */
    VehicleContract getById(Long id);

    /**
     *
     * Chức năng: Kiểm tra vehicle có thuộc quyền sở hữu của customer hay không.
     *
     * Quy trình:
     * - Nhận vehicleId và customerId cần kiểm tra.
     * - Tìm kiếm vehicle tương ứng.
     * - So sánh customer sở hữu vehicle với customer cần xác thực.
     * - Trả về kết quả kiểm tra.
     *
     * @param vehicleId id của vehicle cần xác nhận quyền sở hữu
     * @param customerId id của customer cần kiểm tra quyền sở hữu
     *
     * @return true nếu vehicle thuộc customer, false nếu không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    boolean validateVehicleOwnership(Long vehicleId, Long customerId);

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

    void deleteVehicle(Long customerId, Long vehicleId);

    UpdateVehicleResponse updateVehicle(Long customerId, Long vehicleId, UpdateVehicleRequest request);

}
