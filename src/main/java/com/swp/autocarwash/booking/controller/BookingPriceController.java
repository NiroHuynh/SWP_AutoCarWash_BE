package com.swp.autocarwash.booking.controller;


import com.swp.autocarwash.booking.dto.request.BookingPricePreviewRequest;
import com.swp.autocarwash.booking.dto.response.BookingPricePreviewResponse;
import com.swp.autocarwash.booking.service.BookingPriceService;
import com.swp.autocarwash.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 *
 * Chức năng: BookingPriceController cung cấp các API liên quan đến việc tính toán
 * và xem trước giá booking trước khi thực hiện tạo booking.
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
     *
     * Chức năng: Tính toán và xem trước tổng giá booking trước khi tạo booking.
     *
     * Quy trình:
     * - Nhận thông tin yêu cầu preview giá từ request body.
     * - Gửi dữ liệu đến BookingPriceService để xử lý tính toán.
     * - Tính toán giá service package, addon service và voucher (nếu có).
     * - Trả về kết quả giá booking sau khi tính toán.
     *
     * @param request thông tin dùng để tính toán giá booking bao gồm dịch vụ,
     *                addon và voucher
     *
     * @return BookingPricePreviewResponse chứa thông tin chi tiết giá booking
     *
     * @author Phong
     * @version 1.0
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
