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
            Long userId,
            CreateVehicleRequest request
    ){


        vehicleValidator
                .validateCreate(request);


        // user đăng nhập lấy từ JWWT
        Customer customer =
                customerPort.getCustomerReferenceByUserId(userId);



        Vehicle vehicle =
                Vehicle.builder()
                        .customer(customer)
                        .licensePlate(request.getLicensePlate())
                        .brandName(request.getBrandName())
                        .color(request.getColor())
                        // các field FE ko gửi thì set giá trị mặc định
                        .violationCount(0)
                        .restrictedUntil(null)
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
