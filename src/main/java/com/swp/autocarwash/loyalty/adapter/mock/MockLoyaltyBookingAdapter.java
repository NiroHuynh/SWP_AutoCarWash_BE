package com.swp.autocarwash.loyalty.adapter.mock;

import com.swp.autocarwash.booking.port.LoyaltyPort;
import com.swp.autocarwash.common.contract.loyalty.CustomerTierContract;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MockLoyaltyBookingAdapter implements LoyaltyPort {


    @Override
    public CustomerTierContract getCustomerTier(Integer customerId) {

        // giả lập GOLD tier
        return new CustomerTierContract(
                3,
                "GOLD",
                12, // AC: 12 ngày
                1000,
                new BigDecimal("1.2")
        );
    }

}