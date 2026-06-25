package com.swp.autocarwash.customer.adapter.mock;

import com.swp.autocarwash.auth.port.CustomerPort;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MockCustomerAuthAdapter
        implements CustomerPort {



    /**
     * Mock create customer profile
     * This adapter is used when:
     * - customer module is not ready
     * - local development
     * - integration testing
     *
     * @param customerContract customer data
     */
    @Override
    public void createCustomer(
            CustomerContract customerContract
    ) {

        log.info(
                "Mock create customer profile: {}",
                customerContract
        );

    }


}
