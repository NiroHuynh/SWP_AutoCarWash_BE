package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 *
 * Chức năng: VehicleRepository cung cấp các phương thức truy cập dữ liệu
 * cho Vehicle entity. Repository này hỗ trợ truy vấn, kiểm tra và quản lý
 * dữ liệu vehicle trong database.
 *
 * @author Phong
 * @version 1.0
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     *
     * Chức năng: Lấy danh sách vehicle đang hoạt động thuộc về một customer.
     *
     * Quy trình:
     * - Nhận customerId cần tìm vehicle.
     * - Truy vấn các vehicle có customerId tương ứng.
     * - Lọc các vehicle chưa bị xóa (isDeleted = false).
     * - Trả về danh sách vehicle hợp lệ.
     *
     * @param customerId id của customer cần lấy danh sách vehicle
     *
     * @return danh sách Vehicle thuộc customer và chưa bị xóa
     *
     * @author Phong
     * @version 1.0
     */
    List<Vehicle> findByCustomerIdAndIsDeletedFalse(Long customerId);

    /**
     *
     * Chức năng: Kiểm tra vehicle có tồn tại và thuộc quyền sở hữu của customer hay không.
     *
     * Quy trình:
     * - Nhận vehicleId và customerId cần kiểm tra.
     * - Tìm kiếm vehicle theo id.
     * - Kiểm tra customer sở hữu vehicle.
     * - Trả về kết quả xác thực.
     *
     * @param vehicleId id của vehicle cần kiểm tra
     * @param customerId id của customer cần xác nhận quyền sở hữu
     *
     * @return true nếu vehicle tồn tại và thuộc customer, false nếu không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    boolean existsByIdAndCustomerId(Long vehicleId, Long customerId);
}
