package com.swp.autocarwash.customer.mapper;

import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.customer.entity.Vehicle;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Chức năng: VehicleMapper dùng để chuyển đổi dữ liệu giữa Vehicle entity và
 * VehicleContract. Class này hỗ trợ expose dữ liệu vehicle giữa các module
 * thông qua contract layer.
 *
 * @author Phong
 * @version 1.0
 */
@Component
public class VehicleMapper {

    private final ModelMapper modelMapper;


    public VehicleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     *
     * Chức năng: Chuyển đổi Vehicle entity sang VehicleContract để sử dụng
     * trong giao tiếp giữa vehicle module và các module khác.
     *
     * Quy trình:
     * - Nhận Vehicle entity từ service layer.
     * - Sử dụng ModelMapper để mapping các field tương ứng.
     * - Lấy customerId từ entity customer liên kết.
     * - Gán customerId vào VehicleContract.
     * - Trả về VehicleContract hoàn chỉnh.
     *
     * @param vehicle entity Vehicle cần chuyển đổi
     *
     * @return VehicleContract chứa dữ liệu vehicle dạng contract
     *
     * @author Phong
     * @version 1.0
     */
    public VehicleContract toContract(Vehicle vehicle) {
        VehicleContract contract = modelMapper.map(vehicle, VehicleContract.class);
        contract.setCustomerId(vehicle.getCustomer().getId());
        return contract;
    }

    /**
     *
     * Chức năng: Chuyển đổi danh sách Vehicle entity sang danh sách VehicleContract.
     *
     * Quy trình:
     * - Nhận danh sách Vehicle entity.
     * - Duyệt qua từng phần tử trong danh sách.
     * - Chuyển đổi từng Vehicle bằng method toContract().
     * - Trả về danh sách VehicleContract tương ứng.
     *
     * @param vehicles danh sách Vehicle entity cần chuyển đổi
     *
     * @return danh sách VehicleContract sau khi mapping
     *
     * @author Phong
     * @version 1.0
     */
    public List<VehicleContract> toContracts(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(this::toContract)
                .collect(Collectors.toList());
    }
}
