package com.swp.autocarwash.customer.service.customer;

import com.swp.autocarwash.common.contract.customer.CustomerContract;

/**
 *
 * Business logic xử lý customer
 *
 * @author Phong
 * @version 1.0
 */
public interface CustomerService {


    /**
     *
     * Lấy customer contract theo id
     *
     * @param id customer id
     *
     * @return CustomerContract
     *
     * @author Phong
     * @version 1.0
     */
    CustomerContract getCustomerById(Integer id);



    /**
     *
     * Kiểm tra customer có thể booking
     *
     * @param id customer id
     *
     * @return true nếu hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    boolean isEligibleForBooking(Integer id);

}
