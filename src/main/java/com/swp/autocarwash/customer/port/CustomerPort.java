package com.swp.autocarwash.customer.port;

import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.customer.entity.Customer;

public interface CustomerPort {


    /**
     * Get customer reference
     *
     * @param customerId customer id
     * @return customer entity reference
     */
    Customer getCustomerReference(
            Long customerId
    );

}
