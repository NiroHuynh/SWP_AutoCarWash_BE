package com.swp.autocarwash.customer.service.customer;


import com.swp.autocarwash.customer.entity.Customer;

/**
 *
 * Chức năng: CustomerService định nghĩa các phương thức xử lý nghiệp vụ
 * liên quan đến customer. Interface này cung cấp các chức năng truy vấn thông tin
 * customer và kiểm tra điều kiện sử dụng dịch vụ booking.
 *
 * @author Phong
 * @version 1.0
 */
public interface CustomerService {


    /**
     *
     * Chức năng: Lấy thông tin customer theo customer id.
     *
     * Quy trình:
     * - Nhận id của customer cần tìm.
     * - Truy vấn dữ liệu customer từ repository.
     * - Trả về Customer entity tương ứng.
     *
     * @param id id của customer cần lấy thông tin
     *
     * @return Customer entity chứa thông tin customer
     *
     * @author Phong
     * @version 1.0
     */
    Customer getCustomerById(Long id);



    /**
     *
     * Chức năng: Kiểm tra customer có đủ điều kiện thực hiện booking hay không.
     *
     * Quy trình:
     * - Nhận customer id cần kiểm tra.
     * - Kiểm tra trạng thái hoạt động của customer.
     * - Kiểm tra các điều kiện hạn chế booking nếu có.
     * - Trả về kết quả xác thực.
     *
     * @param id id của customer cần kiểm tra điều kiện booking
     *
     * @return true nếu customer hợp lệ để booking, false nếu không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    boolean isEligibleForBooking(Long id);


    Customer getCustomerByUserId(Long userId);


    void updateTier(Long customerId, Integer customerTierId);
}
