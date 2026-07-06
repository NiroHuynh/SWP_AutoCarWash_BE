package com.swp.autocarwash.loyalty.port;

public interface CustomerPort {

    void updateCustomerTier(
            Long customerId,
            Integer customerTierId
    );
}
