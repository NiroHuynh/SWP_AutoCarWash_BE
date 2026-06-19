package com.swp.autocarwash.customer.service.customer.impl;

import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.mapper.CustomerMapper;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.customer.service.customer.CustomerService;
import com.swp.autocarwash.customer.validator.CustomerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * Implementation business logic customer
 *
 * @author Phong
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {



    private final CustomerRepository repository;

    private final CustomerMapper mapper;

    private final CustomerValidator validator;




    /**
     *
     * Lấy customer theo id
     *
     * @param id customer id
     *
     * @return customer contract
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerContract getCustomerById(Integer id){

        Customer customer =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        ErrorCode.CUSTOMER_NOT_FOUND
                                )
                        );

        return mapper.toContract(customer);

    }





    /**
     *
     * Kiểm tra customer có được phép booking
     *
     * @param id customer id
     *
     * @return trạng thái booking
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isEligibleForBooking(Integer id){

        Customer customer =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        ErrorCode.CUSTOMER_NOT_FOUND
                                )
                        );


        validator.validateBooking(customer);


        return true;
    }

}
