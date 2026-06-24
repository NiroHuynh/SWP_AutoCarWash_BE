package com.swp.autocarwash.booking.controller;

import com.swp.autocarwash.booking.dto.response.BookingContextResponse;
import com.swp.autocarwash.booking.service.BookingContextService;
import com.swp.autocarwash.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Chức năng: BookingContextController cung cấp các API liên quan đến việc lấy dữ liệu
 * context cần thiết cho quá trình đặt lịch tại station, bao gồm các thông tin phục vụ
 * cho màn hình Schedule.
 *
 * @author Phong
 * @version 1.0
 */
@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class BookingContextController {

    private final BookingContextService bookingContextService;

    /**
     *
     * Chức năng: Lấy toàn bộ dữ liệu context cần thiết cho màn hình Schedule
     * dựa trên station được chọn.
     *
     * Quy trình:
     * - Nhận stationId từ request path.
     * - Gọi BookingContextService để lấy dữ liệu context tương ứng với station.
     * - Đóng gói dữ liệu trả về trong ApiResponse.
     * - Trả kết quả về cho client.
     *
     * @param stationId id của station cần lấy thông tin booking context
     *
     * @return BookingContextResponse chứa toàn bộ dữ liệu cần thiết cho màn hình Schedule
     *
     * @author Phong
     * @version 1.0
     */
    @GetMapping("/{stationId}/booking-context")
    public ApiResponse<BookingContextResponse> getBookingContext(
            @PathVariable Integer stationId
    ) { 
        return ApiResponse.success(
                "Get booking context successfully",
                bookingContextService.getBookingContext(stationId)
        );
    }
}
