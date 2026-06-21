package com.swp.autocarwash.customer.adapter;

import com.swp.autocarwash.booking.port.VehiclePort;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.customer.service.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Chức năng: VehicleBookingAdapter là adapter triển khai VehiclePort trong môi trường production.
 * Class này đóng vai trò cầu nối giữa booking module và vehicle module thông qua VehicleService.
 *
 * Adapter chịu trách nhiệm expose các nghiệp vụ vehicle như lấy danh sách xe,
 * kiểm tra quyền sở hữu và lấy thông tin chi tiết vehicle cho booking flow.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("pro")
@RequiredArgsConstructor
public class VehicleBookingAdapter implements VehiclePort {

    private final VehicleService vehicleService;

    /**
     *
     * Chức năng: Lấy danh sách vehicle thuộc một customer từ vehicle module.
     *
     * Quy trình:
     * - Nhận customerId cần lấy danh sách xe.
     * - Gọi VehicleService để truy vấn dữ liệu vehicle.
     * - Trả về danh sách VehicleContract cho booking module.
     *
     * @param customerId id của customer cần lấy danh sách vehicle
     *
     * @return danh sách VehicleContract chứa thông tin các vehicle của customer
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<VehicleContract> getVehiclesByCustomer(Integer customerId) {
        return vehicleService.getVehiclesByCustomer(customerId);
    }

    /**
     *
     * Chức năng: Lấy id của customer hiện tại đang đăng nhập.
     *
     * Quy trình:
     * - Lấy thông tin authentication từ SecurityContext.
     * - Xác định customer tương ứng với user hiện tại.
     * - Trả về customerId để sử dụng trong booking flow.
     *
     * Hiện tại chưa tích hợp Spring Security hoàn chỉnh.
     *
     * @return id của customer hiện tại
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public Integer getCurrentCustomerId() {
        // TODO: integrate Spring Security later
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     *
     * Chức năng: Kiểm tra vehicle có thuộc quyền sở hữu của customer hay không.
     *
     * Quy trình:
     * - Nhận vehicleId và customerId cần kiểm tra.
     * - Gọi VehicleService để thực hiện validate ownership.
     * - Trả về kết quả kiểm tra.
     *
     * @param vehicleId id của vehicle cần kiểm tra
     * @param customerId id của customer cần xác thực quyền sở hữu
     *
     * @return true nếu vehicle thuộc customer, false nếu không thuộc
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public boolean validateVehicleOwnership(Integer vehicleId, Integer customerId) {
        return vehicleService.validateVehicleOwnership(vehicleId, customerId);
    }

    /**
     *
     * Chức năng: Lấy thông tin chi tiết vehicle theo id.
     *
     * Quy trình:
     * - Nhận vehicleId cần tìm.
     * - Gọi VehicleService để lấy dữ liệu vehicle.
     * - Trả về VehicleContract cho booking module.
     *
     * @param id id của vehicle cần lấy thông tin
     *
     * @return VehicleContract chứa thông tin chi tiết vehicle
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public VehicleContract getById(Integer id) {
        return vehicleService.getById(id);
    }
}
