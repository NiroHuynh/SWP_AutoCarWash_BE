package com.swp.autocarwash.customer.mapper;

import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.customer.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.ZoneId;


/**
 *
 * Chuyển đổi Entity sang Contract
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final ModelMapper modelMapper;

    /**
     *
     * Convert Customer entity sang CustomerContract
     *
     * @param customer entity customer
     *
     * @return CustomerContract
     *
     * @author Phong
     * @version 1.0
     */
    public CustomerContract toContract(Customer customer){


        CustomerContract contract =
                modelMapper.map(
                        customer,
                        CustomerContract.class
                );
        if(customer.getRestrictedUntil()!=null){

            contract.setRestrictedUntil(
                    customer.getRestrictedUntil()
                            .atZone(
                                    ZoneId.systemDefault()
                            )
                            .toLocalDateTime()
            );
        }
        return contract;
    }
}