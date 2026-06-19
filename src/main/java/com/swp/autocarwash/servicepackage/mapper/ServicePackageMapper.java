package com.swp.autocarwash.servicepackage.mapper;

import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


/**
 *
 * Mapper chuyển đổi Entity sang Contract
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ServicePackageMapper {


    private final ModelMapper modelMapper;



    /**
     *
     * Convert ServicePackage entity sang contract
     *
     * @param entity service package entity
     *
     * @return service package contract
     *
     */
    public ServicePackageContract toContract(
            ServicePackage entity
    ){

        ServicePackageContract contract =
                modelMapper.map(
                        entity,
                        ServicePackageContract.class
                );


        /*
         *
         * durationMinutes không tồn tại trong entity
         * map từ requiredSlot
         *
         */
        contract.setDurationMinutes(
                entity.getRequiredSlot()*15
        );


        return contract;
    }

}
