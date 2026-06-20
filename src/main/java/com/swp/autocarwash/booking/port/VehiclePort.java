package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.common.contract.customer.VehicleContract;

import java.util.List;

/**
 *
 * Chức năng: VehiclePort định nghĩa các phương thức giao tiếp giữa booking module
 * và module quản lý vehicle. Interface này cung cấp thông tin phương tiện,
 * xác thực quyền sở hữu xe và lấy dữ liệu xe phục vụ quá trình tạo booking.
 *
 * @author Phong
 * @version 1.0
 */
public interface VehiclePort {
    /**
     *
     * Chức năng: Lấy danh sách phương tiện thuộc về một customer.
     *
     * Quy trình:
     * - Nhận customerId cần lấy danh sách xe.
     * - Gửi yêu cầu lấy dữ liệu vehicle đến module vehicle.
     * - Lấy danh sách xe mà customer sở hữu.
     * - Trả về danh sách VehicleContract.
     *
     * @param customerId id của customer cần lấy danh sách phương tiện
     *
     * @return danh sách VehicleContract chứa thông tin các phương tiện của customer
     *
     * @author Phong
     * @version 1.0
     */
    List<VehicleContract> getVehiclesByCustomer(Integer customerId);

    /**
     *
     * Chức năng: Lấy id của customer hiện tại từ authentication context.
     *
     * Quy trình:
     * - Lấy thông tin user hiện tại từ security context.
     * - Xác định customer tương ứng với user.
     * - Trả về customerId hiện tại.
     *
     * @return id của customer đang đăng nhập
     *
     * @author Phong
     * @version 1.0
     */
    Integer getCurrentCustomerId();

    /**
     *
     * Chức năng: Kiểm tra vehicle có thuộc quyền sở hữu của customer hay không.
     *
     * Quy trình:
     * - Nhận vehicleId và customerId cần kiểm tra.
     * - Tìm kiếm vehicle tương ứng.
     * - Kiểm tra customer sở hữu vehicle.
     * - Trả về kết quả xác thực.
     *
     * @param vehicleId id của vehicle cần kiểm tra
     * @param customerId id của customer cần xác nhận quyền sở hữu
     *
     * @return true nếu vehicle thuộc customer, false nếu không thuộc
     *
     * @author Phong
     * @version 1.0
     */
    boolean validateVehicleOwnership(Integer vehicleId, Integer customerId);

    /**
     *
     * Chức năng: Lấy thông tin vehicle dựa trên vehicleId.
     *
     * Quy trình:
     * - Nhận id của vehicle.
     * - Truy vấn thông tin vehicle tương ứng.
     * - Mapping dữ liệu sang VehicleContract.
     * - Trả về thông tin vehicle.
     *
     * @param id id của vehicle cần lấy thông tin
     *
     * @return VehicleContract chứa thông tin chi tiết vehicle
     *
     * @author Phong
     * @version 1.0
     */
    VehicleContract getById(Integer id);
}
