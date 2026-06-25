package com.swp.autocarwash.customer.adapter;

import com.swp.autocarwash.booking.port.CustomerPort;

import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.common.contract.loyalty.CustomerTierContract;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.service.customer.CustomerService;
import com.swp.autocarwash.loyalty.entity.CustomerTier;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


/**
 *
 * Chức năng: CustomerBookingAdapter là adapter expose customer module cho booking module
 * thông qua CustomerPort. Class này chịu trách nhiệm chuyển đổi dữ liệu từ Customer entity
 * sang CustomerContract và cung cấp các nghiệp vụ liên quan đến customer phục vụ booking.
 *
 * Adapter này được sử dụng trong môi trường production để kết nối trực tiếp với
 * customer service thật thay vì dữ liệu mock.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("pro")
@RequiredArgsConstructor
public class CustomerBookingAdapter implements CustomerPort {


    private final ModelMapper modelMapper;
    private final CustomerService customerService;




    /**
     *
     * Chức năng: Lấy thông tin customer thông qua customer module và chuyển đổi
     * sang CustomerContract để booking module sử dụng.
     *
     * Quy trình:
     * - Nhận customerId cần lấy thông tin.
     * - Gọi customerService để truy vấn customer.
     * - Mapping Customer entity sang CustomerContract.
     * - Trả về dữ liệu customer dạng contract.
     *
     * @param customerId id của customer cần lấy thông tin
     *
     * @return CustomerContract chứa thông tin customer
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public CustomerContract getCustomerById(
            Integer customerId){

        Customer customer = customerService
                .getCustomerById(customerId);
        return modelMapper.map(customer, CustomerContract.class);
    }





    /**
     *
     * Chức năng: Kiểm tra customer có đủ điều kiện để thực hiện booking hay không.
     *
     * Quy trình:
     * - Nhận customerId cần kiểm tra.
     * - Gọi customerService thực hiện validate rule customer.
     * - Trả về kết quả customer có được phép booking.
     *
     * @param customerId id của customer cần kiểm tra
     *
     * @return true nếu customer hợp lệ để booking, false nếu không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public boolean isEligibleForBooking(
            Integer customerId){


        return customerService
                .isEligibleForBooking(customerId);
    }

    /**
     *
     * Chức năng: Lấy thông tin tier hiện tại của customer phục vụ
     * tính toán quyền lợi booking như booking window.
     *
     * Quy trình:
     * - Nhận customerId cần lấy tier.
     * - Lấy thông tin customer từ customerService.
     * - Truy cập customerTier của customer.
     * - Mapping CustomerTier entity sang CustomerTierContract.
     * - Trả về thông tin tier.
     *
     * @param customerId id của customer cần lấy tier
     *
     * @return CustomerTierContract chứa thông tin tier của customer
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public CustomerTierContract getTierOfCustomer(Integer customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        System.out.println(customer.getFirstName());
        return modelMapper.map(customer.getCustomerTier(), CustomerTierContract.class);
    }


}
