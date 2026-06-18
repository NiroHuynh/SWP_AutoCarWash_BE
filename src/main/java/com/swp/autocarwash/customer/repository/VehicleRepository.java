package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 *
 * Repository thao tác dữ liệu Vehicle
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
