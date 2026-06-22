package com.swp.autocarwash.customer.service.customer.impl;

import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.mapper.CustomerMapper;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.customer.service.customer.CustomerService;
import com.swp.autocarwash.customer.validator.CustomerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * Chức năng: CustomerServiceImpl triển khai các nghiệp vụ liên quan đến customer.
 * Class này chịu trách nhiệm xử lý logic lấy thông tin customer, kiểm tra điều kiện
 * customer trước khi thực hiện các chức năng như booking.
 *
 * @author Phong
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {



    private final CustomerRepository repository;

    private final CustomerMapper mapper;

    private final CustomerValidator validator;




    /**
     *
     * Chức năng: Lấy thông tin customer theo id.
     *
     * Quy trình:
     * - Nhận customer id cần tìm kiếm.
     * - Chuyển đổi id sang kiểu dữ liệu phù hợp với repository.
     * - Tìm kiếm customer trong database.
     * - Nếu không tồn tại customer thì ném ResourceNotFoundException.
     * - Trả về Customer entity tìm được.
     *
     * @param id id của customer cần lấy thông tin
     *
     * @return Customer entity chứa thông tin customer
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id){

        Customer customer =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        ErrorCode.CUSTOMER_NOT_FOUND
                                )
                        );

        return customer;

    }





    /**
     *
     * Chức năng: Kiểm tra customer có đủ điều kiện để thực hiện booking hay không.
     *
     * Quy trình:
     * - Nhận customer id cần kiểm tra.
     * - Tìm kiếm customer trong database.
     * - Nếu customer không tồn tại thì throw ResourceNotFoundException.
     * - Gửi customer qua CustomerValidator để kiểm tra các rule nghiệp vụ.
     * - Nếu validate thành công thì customer được phép booking.
     *
     * @param id id của customer cần kiểm tra điều kiện booking
     *
     * @return true nếu customer hợp lệ để booking
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isEligibleForBooking(Long id){

        Customer customer =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        ErrorCode.CUSTOMER_NOT_FOUND
                                )
                        );


        validator.validateBooking(customer);


        return true;
    }

    @Override
    public Customer getCustomerByUserId(Long userId) {
        return repository.findCustomerByUserId(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                ErrorCode.CUSTOMER_NOT_FOUND
                        )
                );
    }
}
