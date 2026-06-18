package com.swp.autocarwash.customer.mapper;

import com.swp.autocarwash.customer.dto.response.CreateVehicleResponse;
import com.swp.autocarwash.customer.entity.Vehicle;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


/**
 *
 * Mapper chuyển đổi Vehicle entity và DTO
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class VehicleMapper {


    private final ModelMapper modelMapper;



    /**
     *
     * Convert Vehicle entity sang response DTO
     *
     * @param vehicle entity vehicle
     * @return CreateVehicleResponse
     */
    public CreateVehicleResponse toResponse(
            Vehicle vehicle
    ){

        CreateVehicleResponse response =
                modelMapper.map(
                        vehicle,
                        CreateVehicleResponse.class
                );


        response.setCustomerId(
                Integer.parseInt(vehicle.getCustomer().getId().toString())
        );


        return response;
    }


}
