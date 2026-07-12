package com.swp.autocarwash.booking.controller;

import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.booking.dto.response.BookingCardResponse;
import com.swp.autocarwash.booking.dto.response.BookingDetailResponse;
import com.swp.autocarwash.booking.dto.response.StationBookingListPageResponse;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.staff.util.StaffScopeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.swp.autocarwash.booking.dto.request.CreateBookingRequest;
import com.swp.autocarwash.booking.dto.response.CreateBookingResponse;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Controller xử lý các yêu cầu HTTP liên quan đến lịch đặt xe.
 *
 * <p>Tất cả phản hồi đều được bọc trong {@link ApiResponse} để đảm bảo
 * cấu trúc response nhất quán trong toàn bộ ứng dụng.</p>
 *
 * <p>Base URL: {@code /api/bookings}</p>
 *
 * @author KimNgan, Phong
 * @version 1.0
 */


@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final CustomerRepository customerRepository;
    private final StaffScopeResolver staffScopeResolver;

    /**
     * Suy ra customerId của khách hàng đang đăng nhập từ token
     *
     * @param principal thông tin user đang đăng nhập, lấy từ JWT đã xác thực
     * @return id của customer gắn với user đang đăng nhập
     */
    private Long resolveCustomerId(UserCustomerDetails principal) {
        Customer customer = customerRepository.findByUserId(principal.getUser().getId());
        if (customer == null) {
            throw new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        return customer.getId();
    }

    /**
     * Lấy danh sách lịch đặt sắp tới của khách hàng đang đăng nhập (tab "Upcoming Appointments").
     *
     * <p>Chỉ trả về các booking có trạng thái {@code CONFIRMED}, {@code CHECK_IN}
     * hoặc {@code WASHING}, sắp xếp theo thời gian hẹn gần nhất lên đầu.
     * Đáp ứng AC-25.1.2 và AC-25.1.3.</p>
     *
     * <p><b>Ví dụ:</b> {@code GET /api/bookings/upcoming}</p>
     *
     * @param principal khách hàng đang đăng nhập, lấy từ JWT đã xác thực
     * @return {@code 200 OK} với danh sách {@link BookingCardResponse};
     *         trả về danh sách rỗng nếu không có booking nào thỏa điều kiện
     */
    // Code/Javadoc gốc:
    // * <p><b>Ví dụ:</b> {@code GET /api/bookings/upcoming?customerId=1}</p>
    // * @param customerId mã định danh của khách hàng cần lấy danh sách lịch đặt
    // @GetMapping("/upcoming")
    // public ResponseEntity<ApiResponse<List<BookingCardResponse>>> getUpcomingBookings(
    //         @RequestParam Long customerId) {
    //
    // Lý do đổi: endpoint nhận customerId trực tiếp từ client (query param), không kiểm tra
    // customerId đó có thuộc về user đang đăng nhập hay không -> lỗ hổng IDOR (Insecure Direct
    // Object Reference): user đã login đổi được customerId trên URL để xem lịch sử của người khác.
    // Sửa lại: customerId luôn suy ra từ token đăng nhập qua resolveCustomerId(principal).
    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<BookingCardResponse>>> getUpcomingBookings(
            @AuthenticationPrincipal UserCustomerDetails principal) {

        Long customerId = resolveCustomerId(principal);
        List<BookingCardResponse> result = bookingService.getUpcomingBookings(customerId);

        if (result.isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.success("Không có lịch đặt sắp tới", Collections.emptyList())
            ); // trả list rỗng chứ ko trả null
        }

        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách lịch đặt sắp tới thành công", result)
        );
    }

    /**
     * Lấy danh sách lịch sử dịch vụ của khách hàng đang đăng nhập (tab "Past Services").
     *
     * <p>Chỉ trả về các booking có trạng thái {@code PAID}, {@code CANCELED}
     * hoặc {@code NO_SHOW}, sắp xếp theo thời gian hẹn mới nhất lên đầu.
     * Đáp ứng AC-25.2.1, AC-25.2.3, AC-25.2.4, AC-25.2.5.</p>
     *
     * <p><b>Ví dụ:</b> {@code GET /api/bookings/past}</p>
     *
     * @param principal khách hàng đang đăng nhập, lấy từ JWT đã xác thực
     * @return {@code 200 OK} với danh sách {@link BookingCardResponse};
     *         trả về danh sách rỗng nếu không có lịch sử nào
     */
    // Code/Javadoc gốc:
    // * <p><b>Ví dụ:</b> {@code GET /api/bookings/past?customerId=1}</p>
    // * @param customerId mã định danh của khách hàng cần lấy lịch sử dịch vụ
    // @GetMapping("/past")
    // public ResponseEntity<ApiResponse<List<BookingCardResponse>>> getPastBookings(
    //         @RequestParam Long customerId) {
    //
    // Lý do đổi: cùng lý do với getUpcomingBookings() ở trên - endpoint nhận customerId trực tiếp
    // từ client (query param), không kiểm tra customerId đó có thuộc về user đang đăng nhập hay
    // không -> lỗ hổng IDOR. Sửa lại: customerId luôn suy ra từ token đăng nhập qua resolveCustomerId(principal).
    @GetMapping("/past")
    public ResponseEntity<ApiResponse<List<BookingCardResponse>>> getPastBookings(
            @AuthenticationPrincipal UserCustomerDetails principal) {

        Long customerId = resolveCustomerId(principal);
        List<BookingCardResponse> result = bookingService.getPastBookings(customerId);

        if (result.isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.success("Bạn chưa có lịch sử dịch vụ nào.", Collections.emptyList())
            );
        }

        return ResponseEntity.ok(
                ApiResponse.success("Lấy lịch sử dịch vụ thành công", result)
        );
    }

    /**
     * Lấy thông tin chi tiết của một lịch đặt theo ID (màn hình "Booking Detail").
     *
     * <p>Trả về đầy đủ thông tin: dịch vụ, xe, chi nhánh, lịch hẹn, kỹ thuật viên
     * và chi tiết thanh toán theo AC-25.3.2.</p>
     *
     * <p><b>Ví dụ:</b> {@code GET /api/bookings/1}</p>
     *
     * @param bookingId mã định danh của lịch đặt cần xem
     * @return {@code 200 OK} với {@link BookingDetailResponse};
     *         {@code 404 Not Found} nếu không tìm thấy booking
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDetailResponse>> getBookingDetail(
            @PathVariable Long bookingId) {

        BookingDetailResponse result = bookingService.getBookingDetail(bookingId);

        return ResponseEntity.ok(
                ApiResponse.success("Lấy thông tin chi tiết booking thành công", result)
        );
    }

    /**
     * Hủy một lịch đặt theo AC-23.1.1.
     *
     * <p>Sau khi hủy, slot đã đặt được giải phóng cho khách hàng khác.
     * Không xử lý hoàn tiền cọc (nghiệp vụ ngoài hệ thống).</p>
     *
     * <p><b>Ví dụ:</b> {@code PATCH /api/bookings/1/cancel}</p>
     *
     * @param bookingId mã định danh của lịch đặt cần hủy
     * @return {@code 200 OK} với {@link BookingDetailResponse} (status = CANCELED);
     *         {@code 404 Not Found} nếu không tìm thấy booking
     */
    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<BookingDetailResponse>> cancelBooking(
            @PathVariable Long bookingId) {

        BookingDetailResponse result = bookingService.cancelBooking(bookingId);

        return ResponseEntity.ok(
                ApiResponse.success("Hủy lịch đặt thành công", result)
        );
    }
    /**
     *
     * Chức năng: Tạo mới một booking.
     *
     * Quy trình:
     * - Nhận thông tin booking từ request body.
     * - Gọi BookingService để xử lý tạo booking.
     * - Kiểm tra slot, giá tiền và dữ liệu liên quan trong service layer.
     * - Trả về thông tin booking sau khi tạo thành công.
     *
     * @param request thông tin cần thiết để tạo booking mới
     *
     * @return CreateBookingResponse chứa thông tin booking vừa được tạo
     *
     * @author Phong
     * @version 1.0
     */
    @PostMapping
    public ApiResponse<CreateBookingResponse> create(@RequestBody CreateBookingRequest request) {

        return ApiResponse.success(
                "Booking created successfully",
                bookingService.createBooking(request)
        );
    }

    /**
     * Chức năng: Staff/Admin xem danh sách toàn bộ booking của 1 chi nhánh
     * (staff bị pin cứng về station của mình, admin phải truyền stationId).
     *
     * <p><b>Ví dụ:</b> {@code GET /api/bookings/station?status=CHECK_IN&keyword=0912}</p>
     *
     * @param stationId chi nhánh cần xem — bắt buộc với ADMIN, bị bỏ qua/ép lại với STAFF
     * @param status    giá trị thô của BookingStatus, bỏ trống = không lọc
     * @param fromDate  lọc appointmentDate từ ngày này trở đi, bỏ trống = không giới hạn
     * @param toDate    lọc appointmentDate đến ngày này, bỏ trống = không giới hạn
     * @param keyword   tìm theo tên khách/SĐT/biển số, bỏ trống = không lọc
     * @author Ngân
     * @version 1.0
     */
    @GetMapping("/station")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<StationBookingListPageResponse>> getStationBookingList(
            @AuthenticationPrincipal UserCustomerDetails principal,
            @RequestParam(required = false) Integer stationId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Integer effectiveStationId = staffScopeResolver.resolveStationId(principal, stationId);
        if (effectiveStationId == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
        StationBookingListPageResponse data = bookingService.getStationBookingList(
                effectiveStationId, status, fromDate, toDate, keyword, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success("Danh sách booking", data));
    }
}
