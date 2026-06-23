package com.swp.autocarwash.customer.repository.custom;

import com.swp.autocarwash.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}