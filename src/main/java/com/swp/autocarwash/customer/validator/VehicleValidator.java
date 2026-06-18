package com.swp.autocarwash.customer.validator;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.dto.request.CreateVehicleRequest;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 *
 * Validator xử lý các rule nghiệp vụ liên quan Vehicle
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class VehicleValidator {


    private final VehicleRepository vehicleRepository;



    /**
     *
     * Kiểm tra dữ liệu trước khi tạo vehicle
     *
     * @param request request tạo xe
     */
    public void validateCreate(
            CreateVehicleRequest request
    ) {


        if(
                vehicleRepository
                        .existsByLicensePlate(
                                request.getLicensePlate()
                        )
        ){

            throw new BusinessException(
                    ErrorCode.LICENSE_PLATE_ALREADY_EXISTS
            );

        }

    }


}