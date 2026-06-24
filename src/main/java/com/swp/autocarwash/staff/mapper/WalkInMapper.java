package com.swp.autocarwash.staff.mapper;

import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.staff.dto.response.CheckPhoneResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface WalkInMapper {
    //tự động chuyển đổi entity Vehicle -> VehicleDTO
    CheckPhoneResponse.VehicleDTO toVehicleDTO(Vehicle vehicle);

    //Tự chuyển cả list entity -> list dto
    List<CheckPhoneResponse.VehicleDTO> toVehicleDTOList(List<Vehicle> vehicles);

    @Mapping(target="existed", constant="true")
    @Mapping(target = "customerName", expression = "java(customer.getFirstName() + \" \" + customer.getLastName())")
    @Mapping(source = "customer.customerTier.tierName", target = "tierName")
    @Mapping(source = "savedVehicles", target = "savedVehicles")
    CheckPhoneResponse toCheckPhoneResponse(Customer customer, List<CheckPhoneResponse.VehicleDTO> savedVehicles);
}
