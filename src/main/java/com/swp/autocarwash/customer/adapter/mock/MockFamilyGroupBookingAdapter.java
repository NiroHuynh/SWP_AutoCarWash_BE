package com.swp.autocarwash.customer.adapter.mock;

import com.swp.autocarwash.booking.port.FamilyGroupPort;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MockFamilyGroupBookingAdapter implements FamilyGroupPort {
    @Override
    public Long getOwnerCustomerIdOfCustomerId(Long customerId) {
        return 0L;
    }
}
