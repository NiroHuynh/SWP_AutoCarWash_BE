package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


/**
 *
 * Repository thao tác database cho Customer entity
 *
 * @author Phong
 * @version 1.0
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    /**
     *
     * Tìm customer theo id
     *
     * @param id customer id
     * @return customer nếu tồn tại
     *
     * @author Phong
     * @version 1.0
     */
    Optional<Customer> findById(Long id);

}
