package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface VehicleRepository
        extends JpaRepository<Vehicle, Integer> {



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

}
