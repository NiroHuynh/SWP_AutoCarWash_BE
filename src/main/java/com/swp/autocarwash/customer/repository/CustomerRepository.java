package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


/**
 *
 * Chức năng: CustomerRepository cung cấp các phương thức truy cập dữ liệu
 * cho Customer entity. Repository này hỗ trợ thao tác CRUD và truy vấn customer
 * trong database.
 *
 * @author Phong
 * @version 1.0
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    /**
     *
     * Chức năng: Tìm kiếm customer theo customer id.
     *
     * Quy trình:
     * - Nhận customerId cần tìm kiếm.
     * - Thực hiện truy vấn customer trong database.
     * - Trả về Optional chứa Customer nếu tồn tại.
     * - Trả về Optional.empty() nếu không tìm thấy customer.
     *
     * @param id id của customer cần tìm
     *
     * @return Optional<Customer> chứa customer nếu tồn tại
     *
     * @author Phong
     * @version 1.0
     */
    Optional<Customer> findById(Long id);

}
