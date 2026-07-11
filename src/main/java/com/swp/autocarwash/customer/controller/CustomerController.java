package com.swp.autocarwash.customer.controller;

import com.swp.autocarwash.auth.dto.request.UpdateProfileRequest;
import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.booking.dto.response.CustomerBookingHistoryPageResponse;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.dto.response.CustomerDetailResponse;
import com.swp.autocarwash.customer.dto.response.CustomerListPageResponse;
import com.swp.autocarwash.customer.dto.response.CustomerProfileResponse;
import com.swp.autocarwash.customer.dto.response.CustomerUpdateProfileResponse;
import com.swp.autocarwash.customer.service.customer.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<CustomerProfileResponse>> getProfile(@AuthenticationPrincipal UserCustomerDetails userDetails) {
        //Bốc thẳng ID từ hộp lưu giữ trên RAM
        Long loggedInCustomerId = userDetails.getCustomerId();

        CustomerProfileResponse response = customerService.getCustomerProfile(loggedInCustomerId);
        return ResponseEntity.ok(ApiResponse.success(response.getMessage(), response));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<CustomerUpdateProfileResponse>> updateProfile(
            @AuthenticationPrincipal UserCustomerDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request) {

        // 1. Lấy customerId an toàn từ Token đã giải mã ở Filter
        Long customerId = userDetails.getCustomerId();
        // 2. Gọi xuống Service xử lý
        CustomerUpdateProfileResponse response = customerService.updateCustomerProfile(customerId, request);
        // 3. Trả về thông báo thành công
        return ResponseEntity.ok(ApiResponse.success("Your information has been updated successfully.",response));
    }

    /**
     * Chức năng: Admin xem danh sách khách hàng, kèm 3 thẻ KPI (FE-US-09).
     *
     * @param year   lọc theo năm đăng ký tài khoản (User.createdAt), bỏ trống = không lọc
     * @param month  lọc theo tháng đăng ký tài khoản (1-12), bỏ trống = không lọc
     * @param tier   lọc theo hạng thành viên (MEMBER/SILVER/GOLD/PLATINUM), bỏ trống = không lọc
     * @param active     lọc theo trạng thái tài khoản (true=hoạt động/false=đã khoá), bỏ trống = không lọc
     * @param stationId  lọc theo chi nhánh cụ thể (khách có ít nhất 1 booking đã CHECK_OUT tại chi nhánh này), bỏ trống = không lọc
     * @param communeId  lọc theo xã/phường (khách có ít nhất 1 booking đã CHECK_OUT tại chi nhánh nào đó thuộc xã/phường này), bỏ trống = không lọc
     * @param provinceId lọc theo tỉnh/thành (khách có ít nhất 1 booking đã CHECK_OUT tại chi nhánh nào đó thuộc tỉnh/thành này), bỏ trống = không lọc
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerListPageResponse>> getCustomerList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) String tier,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Integer stationId,
            @RequestParam(required = false) Integer communeId,
            @RequestParam(required = false) Integer provinceId) {
        Pageable pageable = PageRequest.of(page, size);
        CustomerListPageResponse data = customerService.getCustomerList(
                keyword, year, month, tier, active, stationId, communeId, provinceId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Danh sách khách hàng", data));
    }

    /**
     * Chức năng: Overlay chi tiết khách hàng cho Admin (FE-US-09-03 AC1/AC2).
     */
    @GetMapping("/{customerId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerDetailResponse>> getCustomerDetail(@PathVariable Long customerId) {
        CustomerDetailResponse data = customerService.getCustomerDetail(customerId);
        return ResponseEntity.ok(ApiResponse.success("Chi tiết khách hàng", data));
    }

    /**
     * Chức năng: Admin xem toàn bộ lịch sử đặt lịch của khách hàng trên mọi
     * chi nhánh, lọc theo phương tiện/loại dịch vụ/trạng thái/chi nhánh (FE-US-09-04).
     *
     * @param vehicleKeyword    từ khóa tìm theo biển số/hãng xe (>=2 ký tự mới áp dụng lọc, AC2)
     * @param serviceCategoryId lọc theo loại dịch vụ, bỏ trống = không lọc (AC3)
     * @param status            giá trị thô của BookingStatus (PENDING/CONFIRMED/CANCELED/CHECK_IN/
     *                          NO_SHOW/WASHING/COMPLETED/CHECK_OUT), bỏ trống = không lọc (AC4)
     * @param stationId         lọc theo chi nhánh, bỏ trống = tất cả chi nhánh (AC5/AC6)
     * @param year              lọc theo năm đặt lịch (appointmentDate), bỏ trống = không lọc
     * @param month             lọc theo tháng đặt lịch (1-12), bỏ trống = không lọc
     */
    @GetMapping("/{customerId}/bookings")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerBookingHistoryPageResponse>> getCustomerBookingHistory(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String vehicleKeyword,
            @RequestParam(required = false) Integer serviceCategoryId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer stationId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        Pageable pageable = PageRequest.of(page, size);
        CustomerBookingHistoryPageResponse data = customerService.getCustomerBookingHistory(
                customerId, vehicleKeyword, serviceCategoryId, status, stationId, year, month, pageable);
        return ResponseEntity.ok(ApiResponse.success("Lịch sử đặt lịch", data));
    }

    /**
     * Chức năng: Admin xóa (soft-delete) khách hàng — chặn nếu đang có booking
     * khác CANCELED/CHECK_OUT (FE-US-09-03 AC3/AC4).
     */
    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long customerId) {
        List<String> blockingPlates = customerService.deleteCustomer(customerId);
        if (!blockingPlates.isEmpty()) {
            String message = "Không thể xóa: xe " + String.join(", ", blockingPlates) + " đang có đặt lịch hoạt động";
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(message, ErrorCode.CUSTOMER_HAS_ACTIVE_BOOKING.getCode(), null));
        }
        return ResponseEntity.ok(ApiResponse.success("Xóa khách hàng thành công", null));
    }
}
