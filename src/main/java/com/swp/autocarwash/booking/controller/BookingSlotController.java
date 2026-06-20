package com.swp.autocarwash.booking.controller;


import com.swp.autocarwash.booking.dto.request.BookingSlotRequest;
import com.swp.autocarwash.booking.dto.response.BookingSlotResponse;
import com.swp.autocarwash.booking.service.BookingSlotService;
import com.swp.autocarwash.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Chức năng: BookingSlotController cung cấp các API liên quan đến việc lấy danh sách
 * slot thời gian có thể đặt trước tại một trạm rửa xe.
 *
 * @author Phong
 * @version 1.0
 */
@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class BookingSlotController {

    private final BookingSlotService service;

    /**
     *
     * Chức năng: Lấy danh sách các khung giờ còn khả dụng để khách hàng có thể đặt lịch
     * tại một station cụ thể.
     *
     * Quy trình:
     * - Nhận stationId từ request path.
     * - Nhận thông tin tìm kiếm slot từ request body.
     * - Gọi BookingSlotService để kiểm tra và lấy danh sách slot khả dụng.
     * - Trả về danh sách slot có thể đặt cho client.
     *
     * @param stationId id của station cần lấy danh sách slot
     * @param request thông tin yêu cầu tìm kiếm slot bao gồm ngày, thời gian hoặc tiêu chí liên quan
     *
     * @return BookingSlotResponse chứa danh sách các slot có thể đặt
     *
     * @author Phong
     * @version 1.0
     */
    @PostMapping("/{stationId}/booking-slots")
    public ApiResponse<BookingSlotResponse> getSlots(
            @PathVariable Integer stationId,
            @RequestBody BookingSlotRequest request
    ) {
        return ApiResponse.success(
                "Get available slots successfully",
                service.getAvailableSlots(stationId, request)
        );
    }
}
