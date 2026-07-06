package com.swp.autocarwash.customer.validator;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import org.springframework.stereotype.Component;

import java.time.Instant;


/**
 *
 * Validate business rule của customer
 *
 * @author Phong
 * @version 1.0
 */
@Component
public class CustomerValidator {



    /**
     *
     * Kiểm tra customer có được phép booking hay không
     *
     * @param customer customer entity
     *
     * @throws BusinessException nếu customer không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    public void validateBooking(Customer customer){

        if(customer.getRestrictedUntil()!=null
                &&
                customer.getRestrictedUntil()
                        .isAfter(Instant.now())){

            throw new BusinessException(
                    ErrorCode.CUSTOMER_RESTRICTED
            );
        }

        if(customer.getViolationCount()!=null
                &&
                customer.getViolationCount() > 3){

            throw new BusinessException(
                    ErrorCode.CUSTOMER_NOT_ELIGIBLE_FOR_BOOKING
            );
        }
    }

}
