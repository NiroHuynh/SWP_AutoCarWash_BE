package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 *
 * Repository for Vehicle persistence
 *
 * @author Phong
 * @version 1.0
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    List<Vehicle> findByCustomerIdAndIsDeletedFalse(Integer customerId);

    boolean existsByIdAndCustomerId(Integer vehicleId, Integer customerId);
}
