package com.swp.autocarwash.customer.adapter;


import com.swp.autocarwash.customer.service.customer.CustomerService;
import com.swp.autocarwash.loyalty.port.CustomerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("pro")
@RequiredArgsConstructor
public class CustomerLotaltyAdapter implements CustomerPort {

    private final CustomerService customerService;

    @Override
    public void updateCustomerTier(Long customerId, Integer customerTierId) {
        customerService.updateTier(customerId,customerTierId);
    }
}
