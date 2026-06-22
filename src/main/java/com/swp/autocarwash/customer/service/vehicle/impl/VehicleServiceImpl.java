package com.swp.autocarwash.customer.service.vehicle.impl;

import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.mapper.VehicleMapper;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.customer.service.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Chức năng: VehicleServiceImpl triển khai các nghiệp vụ xử lý vehicle.
 * Class này chịu trách nhiệm quản lý logic lấy danh sách xe, lấy thông tin chi tiết
 * vehicle và kiểm tra quyền sở hữu vehicle trước khi sử dụng trong các flow nghiệp vụ.
 *
 * @author Phong
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    /**
     *
     * Chức năng: Lấy danh sách vehicle đang hoạt động của một customer.
     *
     * Quy trình:
     * - Nhận customerId cần lấy danh sách vehicle.
     * - Truy vấn vehicle theo customerId và loại bỏ vehicle đã bị xóa.
     * - Kiểm tra danh sách vehicle có tồn tại hay không.
     * - Nếu không có vehicle thì ném BusinessException.
     * - Mapping danh sách Vehicle entity sang VehicleContract.
     * - Trả về danh sách vehicle của customer.
     *
     * @param customerId id của customer cần lấy danh sách vehicle
     *
     * @return danh sách VehicleContract thuộc customer
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<VehicleContract> getVehiclesByCustomer(Long customerId) {

        List<Vehicle> vehicles =
                vehicleRepository.findByCustomerIdAndIsDeletedFalse(customerId);

        if (vehicles.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_VEHICLE_REGISTERED);
        }

        return vehicleMapper.toContracts(vehicles);
    }

    /**
     *
     * Chức năng: Lấy thông tin chi tiết vehicle theo id.
     *
     * Quy trình:
     * - Nhận vehicleId cần tìm.
     * - Tìm kiếm vehicle trong database.
     * - Nếu không tồn tại vehicle thì throw VEHICLE_NOT_FOUND.
     * - Kiểm tra trạng thái vehicle có bị inactive/xóa hay không.
     * - Mapping Vehicle entity sang VehicleContract.
     * - Trả về thông tin vehicle.
     *
     * @param id id của vehicle cần lấy thông tin
     *
     * @return VehicleContract chứa thông tin chi tiết vehicle
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public VehicleContract getById(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.VEHICLE_NOT_FOUND));

        if (Boolean.TRUE.equals(vehicle.getIsDeleted())) {
            throw new BusinessException(ErrorCode.VEHICLE_INACTIVE);
        }

        return vehicleMapper.toContract(vehicle);
    }

    /**
     *
     * Chức năng: Kiểm tra vehicle có thuộc quyền sở hữu của customer hay không.
     *
     * Quy trình:
     * - Nhận vehicleId và customerId cần xác thực.
     * - Kiểm tra sự tồn tại của quan hệ vehicle - customer trong database.
     * - Nếu vehicle không thuộc customer thì throw VEHICLE_NOT_OWNED.
     * - Trả về kết quả validate.
     *
     * @param vehicleId id của vehicle cần kiểm tra quyền sở hữu
     * @param customerId id của customer cần xác nhận quyền sở hữu
     *
     * @return true nếu vehicle thuộc customer hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    @Transactional(readOnly = true)
    public boolean validateVehicleOwnership(Long vehicleId, Long customerId) {

        boolean exists = vehicleRepository
                .existsByIdAndCustomerId(vehicleId, customerId);

        if (!exists) {
            throw new BusinessException(ErrorCode.VEHICLE_NOT_OWNED);
        }

        return true;
    }
}
