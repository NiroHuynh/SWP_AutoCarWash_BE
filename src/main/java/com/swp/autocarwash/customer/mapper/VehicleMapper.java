package com.swp.autocarwash.customer.mapper;

import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.customer.entity.Vehicle;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Mapper between Vehicle entity and VehicleContract
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
     * Convert entity to contract
     */
    public VehicleContract toContract(Vehicle vehicle) {
        VehicleContract contract = modelMapper.map(vehicle, VehicleContract.class);
        contract.setCustomerId(Integer.parseInt(vehicle.getCustomer().getId().toString()));
        return contract;
    }

    /**
     * Convert list entity to list contract
     */
    public List<VehicleContract> toContracts(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(this::toContract)
                .collect(Collectors.toList());
    }
}
