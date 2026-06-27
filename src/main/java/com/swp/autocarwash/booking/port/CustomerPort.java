package com.swp.autocarwash.booking.port;


import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.common.contract.loyalty.CustomerTierContract;

/**
 *
 * Chức năng: CustomerPort định nghĩa các phương thức giao tiếp giữa booking module
 * và customer module. Interface này cung cấp dữ liệu khách hàng, kiểm tra điều kiện
 * đặt lịch và lấy thông tin tier phục vụ cho các nghiệp vụ booking.
 *
 * @author Phong
 * @version 1.0
 */
public interface CustomerPort {

    /**
     *
     * Chức năng: Lấy thông tin customer dựa trên customerId.
     *
     * Quy trình:
     * - Nhận customerId từ booking flow.
     * - Gửi yêu cầu lấy dữ liệu customer qua port.
     * - Nhận thông tin customer từ module customer.
     * - Trả về CustomerContract để sử dụng trong booking.
     *
     * @param customerId id của customer cần lấy thông tin
     *
     * @return CustomerContract chứa thông tin customer
     *
     * @author Phong
     * @version 1.0
     */
    CustomerContract getCustomerById(Long customerId);

    /**
     *
     * Chức năng: Lấy thông tin customer dựa trên userId.
     *
     * Quy trình:
     * - Nhận userId của tài khoản đang đăng nhập.
     * - Tìm kiếm customer có liên kết với user tương ứng.
     * - Trả về thông tin customer nếu tồn tại.
     *
     * @param userId id của user cần lấy thông tin customer
     *
     * @return customer tương ứng với userId
     *
     * @author Phong
     * @version 1.0
     */
    CustomerContract getCustomerByUserId(Long userId);

    /**
     *
     * Chức năng: Kiểm tra customer có đủ điều kiện để thực hiện đặt lịch hay không.
     *
     * Quy trình:
     * - Nhận customerId cần kiểm tra.
     * - Kiểm tra trạng thái customer như active, blocked hoặc các giới hạn booking.
     * - Trả về kết quả xác định customer có được phép đặt lịch.
     *
     * @param customerId id của customer cần kiểm tra điều kiện booking
     *
     * @return true nếu customer hợp lệ để đặt lịch, false nếu không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    boolean isEligibleForBooking(Long customerId);

    /**
     *
     * Chức năng: Lấy thông tin tier hiện tại của customer.
     *
     * Quy trình:
     * - Nhận customerId cần kiểm tra.
     * - Truy vấn thông tin tier của customer.
     * - Trả về dữ liệu tier dùng để xác định quyền lợi booking.
     *
     * @param customerId id của customer cần lấy tier
     *
     * @return CustomerTierContract chứa thông tin tier của customer
     *
     * @author Phong
     * @version 1.0
     */
    CustomerTierContract getTierOfCustomer(Long customerId);


}
