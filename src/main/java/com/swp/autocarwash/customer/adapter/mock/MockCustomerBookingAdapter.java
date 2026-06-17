package com.swp.autocarwash.customer.adapter.mock;

import com.swp.autocarwash.booking.port.CustomerPort;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import org.springframework.stereotype.Component;

@Component
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
}