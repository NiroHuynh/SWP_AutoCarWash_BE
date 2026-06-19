package com.swp.autocarwash.booking.controller;

import com.swp.autocarwash.booking.dto.request.CreateBookingRequest;
import com.swp.autocarwash.booking.dto.response.CreateBookingResponse;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 *
 * BookingController dùng để xử lý các yêu cầu liên quan đến đặt lịch rửa xe
 *
 * @author Phong
 * @version 1.0
 */

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ApiResponse<CreateBookingResponse> create(@RequestBody CreateBookingRequest request) {

        return ApiResponse.success(
                "Booking created successfully",
                bookingService.createBooking(request)
        );
    }
}