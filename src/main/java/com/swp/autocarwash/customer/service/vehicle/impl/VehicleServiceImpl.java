package com.swp.autocarwash.customer.service.vehicle.impl;

import com.swp.autocarwash.customer.dto.request.CreateVehicleRequest;
import com.swp.autocarwash.customer.dto.response.CreateVehicleResponse;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.mapper.VehicleMapper;
import com.swp.autocarwash.customer.port.CustomerPort;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.customer.service.vehicle.VehicleService;
import com.swp.autocarwash.customer.validator.VehicleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * Implementation xử lý nghiệp vụ Vehicle
 *
 * @author Phong
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl
        implements VehicleService {


    private final VehicleRepository vehicleRepository;

    private final VehicleValidator vehicleValidator;

    private final VehicleMapper vehicleMapper;

    private final CustomerPort customerPort;



    /**
     * Create vehicle
     *
     * Flow:
     * 1. Validate vehicle data
     * 2. Check customer exists via port
     * 3. Create vehicle
     * 4. Save
     * 5. Return response
     */
    @Override
    @Transactional
    public CreateVehicleResponse createVehicle(
            CreateVehicleRequest request
    ){


        vehicleValidator
                .validateCreate(request);



        Customer customer =
                customerPort.getCustomerReference(
                        Long.parseLong(request.getCustomerId().toString())
                );



        Vehicle vehicle =
                Vehicle.builder()
                        .customer(customer)
                        .licensePlate(request.getLicensePlate())
                        .brandName(request.getBrandName())
                        .color(request.getColor())
                        .violationCount(0)
                        .isDeleted(false)
                        .build();

        Vehicle savedVehicle =
                vehicleRepository.save(vehicle);

        return vehicleMapper
                .toResponse(
                        savedVehicle
                );

    }

}
