package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
    List<Vehicle> findByCustomerIdAndIsDeletedFalse(Integer customerId);

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
    boolean existsByIdAndCustomerId(Integer vehicleId, Integer customerId);

    Optional<Vehicle> findByLicensePlateAndIsDeletedFalse(String licensePlate);

    //lấy ta danh sách vehicle của thuộc 1 customer
//    @Query("SELECT v FROM Vehicle v WHERE v.customer.id = :customerId AND v.isDeleted = false")
//    List<Vehicle> findByCustomerIdAndIsDeletedFalse(@Param("customerId") Long customerId);

    List<Vehicle> findByCustomerIdAndIsDeletedFalse(Long customerId);

    //kiểm tra vehicle có tồn tại và thuộc quyền sở hữu của customer hay ko
    boolean existsByIdAndCustomerId(Long vehicleId, Long customerId);

    /**
     *
     * Kiểm tra biển số xe đã tồn tại
     *
     * @param licensePlate biển số
     * @return true nếu tồn tại
     */
    boolean existsByLicensePlate(
            String licensePlate
    );

    /**
     *
     * Tìm xe theo biển số
     *
     * @param licensePlate biển số
     * @return vehicle
     */
    Optional<Vehicle> findByLicensePlate(
            String licensePlate
    );

    // reset chủ động: restricted_until < now tự loại trừ NULL, không cần IS NOT NULL riêng
    List<Vehicle> findByRestrictedUntilBefore(Instant now);

    Optional<Vehicle> findByIdAndIsDeletedFalse(Long vehicleId);

    boolean existsByLicensePlateAndIdNotAndIsDeletedFalse(String licensePlate, Long id);

    Optional<Vehicle> findByIdAndCustomerIdAndIsDeletedFalse(
            Long vehicleId,
            Long customerId
    );



    boolean existsByLicensePlateAndIsDeletedFalse(String licensePlate);
}
