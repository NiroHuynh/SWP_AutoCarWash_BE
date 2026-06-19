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
 * BookingContextController cung cấp API để lấy toàn bộ context cần thiết
 * @Author: Phong
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class BookingContextController {

    private final BookingContextService bookingContextService;

    /**
     * API cung cấp toàn bộ context cho màn hình Schedule
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
