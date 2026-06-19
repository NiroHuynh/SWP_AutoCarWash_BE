package com.swp.autocarwash.booking.controller;


import com.swp.autocarwash.booking.dto.request.BookingSlotRequest;
import com.swp.autocarwash.booking.dto.response.BookingSlotResponse;
import com.swp.autocarwash.booking.service.BookingSlotService;
import com.swp.autocarwash.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 *
 * BookingSlotController cung cấp API để lấy danh sách các khung giờ có thể đặt trước cho một trạm rửa xe
 *
 * @author Phong
 * @version 1.0
 */

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class BookingSlotController {

    private final BookingSlotService service;

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
