package com.swp.autocarwash.booking.service;

import com.swp.autocarwash.booking.dto.request.BookingSlotRequest;
import com.swp.autocarwash.booking.dto.response.BookingSlotResponse;

/**
 *
 * Chức năng: BookingSlotService định nghĩa các phương thức xử lý nghiệp vụ
 * liên quan đến việc kiểm tra và cung cấp các khung giờ booking khả dụng
 * tại station.
 *
 * @author Phong
 * @version 1.0
 */
public interface BookingSlotService {
    /**
     *
     * Chức năng: Lấy danh sách các slot có thể đặt tại một station dựa trên
     * thông tin yêu cầu từ khách hàng.
     *
     * Quy trình:
     * - Nhận stationId và thông tin tìm kiếm slot từ BookingSlotRequest.
     * - Lấy danh sách slot của station theo ngày hoặc điều kiện booking.
     * - Kiểm tra trạng thái khả dụng của từng slot.
     * - Xử lý logic liên tục của slot nếu cần thiết.
     * - Trả về danh sách slot có thể sử dụng để đặt lịch.
     *
     * @param stationId id của station cần lấy danh sách slot
     * @param request thông tin yêu cầu tìm kiếm slot booking
     *
     * @return BookingSlotResponse chứa danh sách slot khả dụng
     *
     * @author Phong
     * @version 1.0
     */
    BookingSlotResponse getAvailableSlots(Integer stationId, BookingSlotRequest request);
}