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
 * Vehicle business logic implementation
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
     * Get all vehicles of a customer
     */
    @Override
    public List<VehicleContract> getVehiclesByCustomer(Integer customerId) {

        List<Vehicle> vehicles =
                vehicleRepository.findByCustomerIdAndIsDeletedFalse(customerId);

        if (vehicles.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_VEHICLE_REGISTERED);
        }

        return vehicleMapper.toContracts(vehicles);
    }

    /**
     * Get vehicle by id
     */
    @Override
    public VehicleContract getById(Integer id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.VEHICLE_NOT_FOUND));

        if (Boolean.TRUE.equals(vehicle.getIsDeleted())) {
            throw new BusinessException(ErrorCode.VEHICLE_INACTIVE);
        }

        return vehicleMapper.toContract(vehicle);
    }

    /**
     * Validate vehicle ownership
     */
    @Override
    @Transactional(readOnly = true)
    public boolean validateVehicleOwnership(Integer vehicleId, Integer customerId) {

        boolean exists = vehicleRepository
                .existsByIdAndCustomerId(vehicleId, customerId);

        if (!exists) {
            throw new BusinessException(ErrorCode.VEHICLE_NOT_OWNED);
        }

        return true;
    }
}
