package com.swp.autocarwash.customer.adapter.mock;

import com.swp.autocarwash.booking.port.CustomerPort;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.common.contract.loyalty.CustomerTierContract;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("dev")
public class MockCustomerBookingAdapter implements CustomerPort {

    @Override
    public CustomerContract getCustomerById(Integer customerId) {
        return new CustomerContract(
                customerId,
                1001,
                "Phong",
                "Huynh",
                0,
                null
        );
    }

    @Override
    public boolean isEligibleForBooking(Integer customerId) {
        // mock rule đơn giản
        return true;
    }

    @Override
    public CustomerTierContract getTierOfCustomer(Integer customerId) {

        return new CustomerTierContract(
                3,
                "GOLD",
                12,
                1000,
                new BigDecimal("1.2")
        );

    }


}