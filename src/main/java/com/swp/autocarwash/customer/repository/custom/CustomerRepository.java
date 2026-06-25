package com.swp.autocarwash.customer.repository.custom;

import com.swp.autocarwash.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserId(
            Long userId
    );
}