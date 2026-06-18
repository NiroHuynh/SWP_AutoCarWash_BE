package com.swp.autocarwash.booking.controller;


import com.swp.autocarwash.booking.dto.request.BookingPricePreviewRequest;
import com.swp.autocarwash.booking.dto.response.BookingPricePreviewResponse;
import com.swp.autocarwash.booking.service.BookingPriceService;
import com.swp.autocarwash.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 *
 * BookingPriceController cung cấp API để preview giá booking trước khi tạo booking
 *
 * @author Phong
 * @version 1.0
 */

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingPriceController {

    private final BookingPriceService bookingPriceService;

    /**
     * Preview total booking price before create booking
     */
    @PostMapping("/preview-price")
    public ApiResponse<BookingPricePreviewResponse> previewPrice(
            @RequestBody BookingPricePreviewRequest request
    ) {
        return ApiResponse.success(
                "Preview price calculated successfully",
                bookingPriceService.calculatePreviewPrice(request)
        );
    }
}
