package com.swp.autocarwash.booking.service;

import com.swp.autocarwash.booking.dto.response.BookingDetailResponse;
import com.swp.autocarwash.booking.dto.response.PastBookingResponse;
import com.swp.autocarwash.booking.dto.response.UpcomingBookingResponse;

import java.util.List;

/**
 * Interface định nghĩa các nghiệp vụ liên quan đến quản lý lịch đặt.
 *
 * <p>Tầng service đóng vai trò trung gian giữa controller và repository,
 * chứa toàn bộ logic nghiệp vụ của module booking.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
public interface BookingService {

    /**
     * Lấy danh sách lịch đặt sắp tới của một khách hàng.
     *
     * <p>Chỉ trả về các booking có trạng thái {@code CONFIRMED}, {@code CHECKED_IN}
     * hoặc {@code WASHING}, sắp xếp theo thời gian hẹn gần nhất lên đầu
     * (theo AC-25.1.2).</p>
     *
     * @param customerId mã định danh của khách hàng đang đăng nhập
     * @return danh sách {@link UpcomingBookingResponse} đã được lọc và sắp xếp;
     *         trả về danh sách rỗng nếu không có booking nào thỏa điều kiện
     */
    List<UpcomingBookingResponse> getUpcomingBookings(Long customerId);

    /**
     * Lấy danh sách lịch sử dịch vụ của một khách hàng.
     *
     * <p>Chỉ trả về các booking có trạng thái {@code PAID}, {@code CANCELLED}
     * hoặc {@code NO_SHOW}, sắp xếp theo thời gian hẹn mới nhất lên đầu
     * (theo AC-25.2.1).</p>
     *
     * @param customerId mã định danh của khách hàng đang đăng nhập
     * @return danh sách {@link PastBookingResponse} đã được lọc và sắp xếp;
     *         trả về danh sách rỗng nếu không có booking nào thỏa điều kiện
     */
    List<PastBookingResponse> getPastBookings(Long customerId);

    /**
     * Lấy thông tin chi tiết đầy đủ của một lịch đặt theo ID.
     *
     * <p>Trả về toàn bộ thông tin cần thiết để render màn hình
     * "Booking Detail" theo AC-25.3.2.</p>
     *
     * @param bookingId mã định danh của lịch đặt cần xem
     * @return {@link BookingDetailResponse} chứa đầy đủ thông tin chi tiết
     * @throws com.swp.autocarwash.common.exception.ResourceNotFoundException
     *         nếu không tìm thấy booking với ID đã cho
     */
    BookingDetailResponse getBookingDetail(Long bookingId);
}
