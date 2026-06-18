package com.swp.autocarwash.customer.adapter.mock;


import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.port.CustomerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MockCustomerCustomerAdapter
        implements CustomerPort {


    @Override
    public Customer getCustomerReference(
            Long customerId
    ) {
        Customer customer = new Customer();
        customer.setId(customerId);
        return customer; // Replace with actual customer retrieval logic
    }


}
