package com.swp.autocarwash.auth.port;

import com.swp.autocarwash.common.contract.customer.CustomerContract;

public interface CustomerPort {

    /**
     * Create customer profile
     *
     * @param contract customer information
     */
    void createCustomer(
            CustomerContract contract
    );


}
