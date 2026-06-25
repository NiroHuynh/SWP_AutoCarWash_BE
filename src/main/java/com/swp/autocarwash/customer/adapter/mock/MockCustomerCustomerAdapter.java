package com.swp.autocarwash.customer.adapter.mock;


import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.port.CustomerPort;
import com.swp.autocarwash.customer.repository.custom.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MockCustomerCustomerAdapter
        implements CustomerPort {

    private final CustomerRepository customerRepository;


    @Override
    public Customer getCustomerReferenceByUserId(
            Long userId
    ) {

        return customerRepository
                .findByUserId(userId)
                .orElseThrow(
                        () -> new BusinessException(
                                ErrorCode.CUSTOMER_NOT_FOUND)
                );

    }


}
