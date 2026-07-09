package com.swp.autocarwash.customer.service.customer;


import com.swp.autocarwash.auth.dto.request.UpdateProfileRequest;
import com.swp.autocarwash.booking.dto.response.CustomerBookingHistoryPageResponse;
import com.swp.autocarwash.customer.dto.response.CustomerDetailResponse;
import com.swp.autocarwash.customer.dto.response.CustomerListPageResponse;
import com.swp.autocarwash.customer.dto.response.CustomerProfileResponse;
import com.swp.autocarwash.customer.dto.response.CustomerUpdateProfileResponse;
import com.swp.autocarwash.customer.entity.Customer;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

    CustomerProfileResponse getCustomerProfile(Long customerId);

    CustomerUpdateProfileResponse updateCustomerProfile(Long customerId, UpdateProfileRequest request);

    /**
     * Chức năng: Danh sách khách hàng cho Admin (FE-US-09) — 3 thẻ KPI +
     * bảng phân trang toàn bộ khách hàng, lọc theo keyword/năm-tháng đăng ký/
     * rank/trạng thái tài khoản.
     */
    CustomerListPageResponse getCustomerList(
            String keyword, Integer year, Integer month, String tier, Boolean active, Pageable pageable);

    /**
     * Chức năng: Overlay chi tiết khách hàng cho Admin (FE-US-09-03 AC1/AC2).
     */
    CustomerDetailResponse getCustomerDetail(Long customerId);

    /**
     * Chức năng: Admin xóa (soft-delete) khách hàng — chặn nếu khách đang có
     * booking khác CANCELED/CHECK_OUT (FE-US-09-03 AC3/AC4).
     *
     * @return danh sách biển số xe đang chặn xóa; rỗng nghĩa là đã xóa thành công
     */
    List<String> deleteCustomer(Long customerId);

    /**
     * Chức năng: Lịch sử đặt lịch của khách hàng trên mọi chi nhánh cho Admin
     * (FE-US-09-04), có filter theo phương tiện/loại dịch vụ/trạng thái/chi nhánh.
     */
    CustomerBookingHistoryPageResponse getCustomerBookingHistory(
            Long customerId, String vehicleKeyword, Integer serviceCategoryId,
            String status, Integer stationId, Integer year, Integer month, Pageable pageable);
}
