package com.swp.autocarwash.customer.mapper;

import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.customer.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.ZoneId;


/**
 *
 * Chức năng: CustomerMapper dùng để chuyển đổi dữ liệu từ Customer entity sang
 * CustomerContract. Class này hỗ trợ việc expose dữ liệu customer giữa các module
 * thông qua contract layer.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final ModelMapper modelMapper;

    /**
     *
     * Chức năng: Chuyển đổi Customer entity sang CustomerContract để sử dụng
     * trong giao tiếp giữa các module.
     *
     * Quy trình:
     * - Nhận Customer entity từ service layer.
     * - Sử dụng ModelMapper để mapping các field tương ứng.
     * - Kiểm tra dữ liệu restrictedUntil có tồn tại hay không.
     * - Convert thời gian restriction sang LocalDateTime nếu có giá trị.
     * - Trả về CustomerContract hoàn chỉnh.
     *
     * @param customer entity Customer cần chuyển đổi
     *
     * @return CustomerContract chứa dữ liệu customer dạng contract
     *
     * @author Phong
     * @version 1.0
     */
    public CustomerContract toContract(Customer customer){


        CustomerContract contract =
                modelMapper.map(
                        customer,
                        CustomerContract.class
                );
        if(customer.getRestrictedUntil()!=null){

            contract.setRestrictedUntil(
                    customer.getRestrictedUntil()
                            .atZone(
                                    ZoneId.systemDefault()
                            )
                            .toLocalDateTime()
            );
        }
        return contract;
    }
}