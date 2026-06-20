package com.swp.autocarwash.customer.adapter.mock;

import com.swp.autocarwash.booking.port.VehiclePort;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Chức năng: MockVehicleBookingAdapter là adapter giả lập triển khai VehiclePort
 * trong môi trường development. Class này cung cấp dữ liệu vehicle mẫu, kiểm tra
 * quyền sở hữu xe và lấy thông tin vehicle mà không cần kết nối tới vehicle module thật.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("dev")
public class MockVehicleBookingAdapter implements VehiclePort {

    /**
     *
     * Chức năng: Lấy danh sách vehicle giả lập thuộc về customer.
     *
     * Quy trình:
     * - Nhận customerId cần lấy danh sách xe.
     * - Tạo danh sách vehicle mẫu.
     * - Trả về danh sách VehicleContract giả lập.
     *
     * @param customerId id của customer cần lấy danh sách vehicle
     *
     * @return danh sách VehicleContract chứa thông tin xe của customer
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<VehicleContract> getVehiclesByCustomer(Integer customerId) {
        return List.of(
                new VehicleContract(1, "71A-12345", "Honda City")
        );
    }

    /**
     *
     * Chức năng: Lấy id customer hiện tại trong môi trường development.
     *
     * Quy trình:
     * - Lấy thông tin customer đang đăng nhập (mock).
     * - Trả về customerId mặc định phục vụ test booking flow.
     *
     * @return id của customer hiện tại
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public Integer getCurrentCustomerId() {
        return 1;
    }

    /**
     *
     * Chức năng: Kiểm tra vehicle có thuộc quyền sở hữu của customer hay không
     * trong môi trường mock.
     *
     * Quy trình:
     * - Nhận vehicleId và customerId cần kiểm tra.
     * - Áp dụng rule giả lập.
     * - Trả về kết quả validate mặc định.
     *
     * @param vehicleId id của vehicle cần kiểm tra
     * @param customerId id của customer cần xác thực quyền sở hữu
     *
     * @return true nếu vehicle hợp lệ, false nếu không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public boolean validateVehicleOwnership(Integer vehicleId, Integer customerId) {
        return true;
    }

    /**
     *
     * Chức năng: Lấy thông tin vehicle giả lập theo id.
     *
     * Quy trình:
     * - Nhận id vehicle cần tìm.
     * - Tạo VehicleContract mẫu với id tương ứng.
     * - Trả về thông tin vehicle để booking module xử lý.
     *
     * @param id id của vehicle cần lấy thông tin
     *
     * @return VehicleContract chứa thông tin vehicle
     *
     * @author Phong
     * @version 1.0
     */

    @Override
    public VehicleContract getById(Integer id) {
        VehicleContract v = new VehicleContract();
        v.setId(id);
        v.setCustomerId(1);
        return v;
    }
}
