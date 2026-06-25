package com.swp.autocarwash.customer.adapter.mock;

import com.swp.autocarwash.booking.port.CustomerPort;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.common.contract.loyalty.CustomerTierContract;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 *
 * Chức năng: MockCustomerBookingAdapter là adapter giả lập triển khai CustomerPort
 * trong môi trường development. Class này cung cấp dữ liệu customer, kiểm tra điều kiện
 * booking và thông tin tier mẫu để phục vụ quá trình phát triển, test mà không cần
 * kết nối trực tiếp tới customer module thật.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("dev")
public class MockCustomerBookingAdapter implements CustomerPort {

    /**
     *
     * Chức năng: Lấy thông tin customer giả lập dựa trên customerId.
     *
     * Quy trình:
     * - Nhận customerId từ booking flow.
     * - Tạo dữ liệu customer mẫu.
     * - Trả về CustomerContract để booking module sử dụng.
     *
     * @param customerId id của customer cần lấy thông tin
     *
     * @return CustomerContract chứa thông tin customer giả lập
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public CustomerContract getCustomerById(Long customerId) {
        return new CustomerContract(
                customerId,
                Long.valueOf(101),
                "Phong",
                "Huynh",
                0,
                null
        );
    }

    /**
     *
     * Chức năng: Kiểm tra customer có đủ điều kiện thực hiện booking hay không
     * trong môi trường mock.
     *
     * Quy trình:
     * - Nhận customerId cần kiểm tra.
     * - Áp dụng rule giả lập.
     * - Trả về kết quả hợp lệ mặc định.
     *
     * @param customerId id của customer cần kiểm tra
     *
     * @return true nếu customer được phép booking, false nếu không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public boolean isEligibleForBooking(Long customerId) {
        // mock rule đơn giản
        return true;
    }

    /**
     *
     * Chức năng: Lấy thông tin tier giả lập của customer.
     *
     * Quy trình:
     * - Nhận customerId cần lấy tier.
     * - Tạo dữ liệu tier mẫu cho môi trường development.
     * - Trả về CustomerTierContract chứa thông tin tier.
     *
     * @param CustomerTierContract id của customer cần lấy tier
     *
     * @return CustomerTierContract chứa thông tin tier giả lập
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public CustomerTierContract getTierOfCustomer(Long customerId) {

        return new CustomerTierContract(
                Long.valueOf(1),
                "GOLD",
                12,
                1000,
                new BigDecimal("1.2")
        );

    }

    @Override
    public CustomerContract getCustomerByUserId(Long userId) {
        return null;
    }
}