package com.swp.autocarwash.booking.service;


import com.swp.autocarwash.booking.dto.request.BookingPricePreviewRequest;
import com.swp.autocarwash.booking.dto.response.BookingPricePreviewResponse;

/**
 *
 * Chức năng: BookingPriceService định nghĩa các phương thức xử lý nghiệp vụ
 * liên quan đến việc tính toán giá booking trước khi khách hàng thực hiện tạo booking.
 *
 * @author Phong
 * @version 1.0
 */
public interface BookingPriceService {

    /**
     *
     * Chức năng: Tính toán giá xem trước của booking dựa trên service package,
     * addon service và voucher được áp dụng.
     *
     * Quy trình:
     * - Nhận thông tin yêu cầu tính giá từ BookingPricePreviewRequest.
     * - Lấy giá service package được chọn.
     * - Tính tổng giá các addon service.
     * - Kiểm tra voucher và tính giá trị giảm giá nếu hợp lệ.
     * - Tổng hợp kết quả giá trước và sau giảm giá.
     * - Trả về thông tin preview giá booking.
     *
     * @param request thông tin yêu cầu tính giá bao gồm service package,
     *                addon service và voucher code
     *
     * @return BookingPricePreviewResponse chứa chi tiết giá booking sau khi tính toán
     *
     * @author Phong
     * @version 1.0
     */
    BookingPricePreviewResponse calculatePreviewPrice(BookingPricePreviewRequest request);
}
