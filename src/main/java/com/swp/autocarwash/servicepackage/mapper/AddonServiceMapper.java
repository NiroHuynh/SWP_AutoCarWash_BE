package com.swp.autocarwash.servicepackage.mapper;

import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.servicepackage.entity.AddonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


/**
 *
 * Mapper chuyển đổi entity và contract của addon service
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class AddonServiceMapper {


    private final ModelMapper modelMapper;


    /**
     *
     * Convert AddonService entity sang contract
     *
     * @param entity addon service entity
     * @return addon service contract
     *
     */
    public AddonServiceContract toContract(
            AddonService entity
    ){

        return modelMapper.map(
                entity,
                AddonServiceContract.class
        );
    }
}