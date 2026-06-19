package com.swp.autocarwash.customer.adapter;

import com.swp.autocarwash.booking.port.CustomerPort;

import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.customer.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


/**
 *
 * Adapter expose customer module cho module khác
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("pro")
@RequiredArgsConstructor
public class CustomerBookingAdapter implements CustomerPort {



    private final CustomerService customerService;




    /**
     *
     * Lấy customer information qua contract
     *
     * @param customerId customer id
     *
     * @return CustomerContract
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public CustomerContract getCustomerById(
            Integer customerId){


        return customerService
                .getCustomerById(customerId);
    }





    /**
     *
     * Kiểm tra customer có thể booking
     *
     * @param customerId customer id
     *
     * @return true nếu hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public boolean isEligibleForBooking(
            Integer customerId){


        return customerService
                .isEligibleForBooking(customerId);
    }

}
