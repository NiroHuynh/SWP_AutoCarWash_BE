package com.swp.autocarwash.servicepackage.validator;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import org.springframework.stereotype.Component;


/**
 *
 * Validator kiểm tra dữ liệu service package
 *
 * @author Phong
 * @version 1.0
 */
@Component
public class ServicePackageValidator {



    /**
     *
     * Validate service package id
     *
     * @param id service package id
     *
     */
    public void validateId(
            Integer id
    ){


        if(id == null || id <= 0){

            throw new BusinessException(
                    ErrorCode.INVALID_SERVICE_PACKAGE_ID
            );

        }

    }


}
