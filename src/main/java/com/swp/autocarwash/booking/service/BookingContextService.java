package com.swp.autocarwash.booking.service;

import com.swp.autocarwash.booking.dto.response.BookingContextResponse;

/**
 *
 * Chức năng: BookingContextService định nghĩa các phương thức xử lý nghiệp vụ
 * liên quan đến việc xây dựng dữ liệu context phục vụ quá trình đặt lịch booking.
 *
 * @author Phong
 * @version 1.0
 */
public interface BookingContextService {

    /**
     *
     * Chức năng: Xây dựng toàn bộ dữ liệu cần thiết cho màn hình Schedule booking
     * dựa trên station được chọn.
     *
     * Quy trình:
     * - Nhận stationId từ booking request.
     * - Lấy thông tin station và các dữ liệu liên quan.
     * - Lấy danh sách vehicle, service package, addon service và voucher khả dụng.
     * - Tổng hợp dữ liệu thành BookingContextResponse.
     * - Trả về context hoàn chỉnh cho client.
     *
     * @param stationId id của station cần lấy dữ liệu booking context
     *
     * @return BookingContextResponse chứa toàn bộ dữ liệu cần thiết cho màn Schedule
     *
     * @author Phong
     * @version 1.0
     */
    BookingContextResponse getBookingContext(Integer stationId);
}
