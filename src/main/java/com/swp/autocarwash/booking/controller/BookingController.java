package com.swp.autocarwash.booking.controller;

import com.swp.autocarwash.booking.dto.response.BookingCardResponse;
import com.swp.autocarwash.booking.dto.response.BookingDetailResponse;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.swp.autocarwash.booking.dto.request.CreateBookingRequest;
import com.swp.autocarwash.booking.dto.response.CreateBookingResponse;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
 * @author KimNgan
 * @version 1.0
 */


@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * Lấy danh sách lịch đặt sắp tới của khách hàng (tab "Upcoming Appointments").
     *
     * <p>Chỉ trả về các booking có trạng thái {@code CONFIRMED}, {@code CHECKED_IN}
     * hoặc {@code WASHING}, sắp xếp theo thời gian hẹn gần nhất lên đầu.
     * Đáp ứng AC-25.1.2 và AC-25.1.3.</p>
     *
     * <p><b>Ví dụ:</b> {@code GET /api/bookings/upcoming?customerId=1}</p>
     *
     * @param customerId mã định danh của khách hàng cần lấy danh sách lịch đặt
     * @return {@code 200 OK} với danh sách {@link BookingCardResponse};
     *         trả về danh sách rỗng nếu không có booking nào thỏa điều kiện
     */
    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<BookingCardResponse>>> getUpcomingBookings(
            @RequestParam Long customerId) {

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
     * Lấy danh sách lịch sử dịch vụ của khách hàng (tab "Past Services").
     *
     * <p>Chỉ trả về các booking có trạng thái {@code PAID}, {@code CANCELLED}
     * hoặc {@code NO_SHOW}, sắp xếp theo thời gian hẹn mới nhất lên đầu.
     * Đáp ứng AC-25.2.1, AC-25.2.3, AC-25.2.4, AC-25.2.5.</p>
     *
     * <p><b>Ví dụ:</b> {@code GET /api/bookings/past?customerId=1}</p>
     *
     * @param customerId mã định danh của khách hàng cần lấy lịch sử dịch vụ
     * @return {@code 200 OK} với danh sách {@link BookingCardResponse};
     *         trả về danh sách rỗng nếu không có lịch sử nào
     */
    @GetMapping("/past")
    public ResponseEntity<ApiResponse<List<BookingCardResponse>>> getPastBookings(
            @RequestParam Long customerId) {

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
     * @return {@code 200 OK} với {@link BookingDetailResponse} (status = CANCELLED);
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

    @PostMapping
    public ApiResponse<CreateBookingResponse> create(@RequestBody CreateBookingRequest request) {

        return ApiResponse.success(
                "Booking created successfully",
                bookingService.createBooking(request)
        );
    }
}
