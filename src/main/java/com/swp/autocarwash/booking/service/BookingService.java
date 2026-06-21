package com.swp.autocarwash.booking.service;

import com.swp.autocarwash.booking.dto.response.BookingCardResponse;
import com.swp.autocarwash.booking.dto.response.BookingDetailResponse;
import com.swp.autocarwash.booking.dto.request.CreateBookingRequest;
import com.swp.autocarwash.booking.dto.response.CreateBookingResponse;

import java.math.BigDecimal;
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
     * @return danh sách {@link BookingCardResponse} đã được lọc và sắp xếp;
     *         trả về danh sách rỗng nếu không có booking nào thỏa điều kiện
     */
    List<BookingCardResponse> getUpcomingBookings(Long customerId);

    /**
     * Lấy danh sách lịch sử dịch vụ của một khách hàng.
     *
     * <p>Chỉ trả về các booking có trạng thái {@code PAID}, {@code CANCELLED}
     * hoặc {@code NO_SHOW}, sắp xếp theo thời gian hẹn mới nhất lên đầu
     * (theo AC-25.2.1).</p>
     *
     * @param customerId mã định danh của khách hàng đang đăng nhập
     * @return danh sách {@link BookingCardResponse} đã được lọc và sắp xếp;
     *         trả về danh sách rỗng nếu không có booking nào thỏa điều kiện
     */
    List<BookingCardResponse> getPastBookings(Long customerId);

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

    /**
     * Hủy một lịch đặt theo AC-23.1.1.
     *
     * <p>Chuyển status booking sang {@code CANCELLED}, ghi nhận
     * {@code canceledAt}, và xóa các {@code BookingSlotAllocation} liên quan
     * để giải phóng slot cho khách hàng khác.</p>
     *
     * @param bookingId mã định danh của lịch đặt cần hủy
     * @return {@link BookingDetailResponse} của booking sau khi hủy (status = CANCELLED)
     * @throws com.swp.autocarwash.common.exception.ResourceNotFoundException
     *         nếu không tìm thấy booking với ID đã cho
     */
    BookingDetailResponse cancelBooking(Long bookingId);

    /**
     *
     * Chức năng: Tạo mới một booking và xử lý toàn bộ logic nghiệp vụ liên quan.
     *
     * Quy trình:
     * - Nhận thông tin tạo booking từ CreateBookingRequest.
     * - Kiểm tra customer, vehicle và dữ liệu booking hợp lệ.
     * - Validate slot có còn khả dụng hay không.
     * - Kiểm tra voucher và áp dụng giảm giá nếu hợp lệ.
     * - Tính toán tổng giá booking.
     * - Lưu thông tin booking và cập nhật trạng thái slot.
     * - Trả về thông tin booking sau khi tạo thành công.
     *
     * @param request thông tin cần thiết để tạo booking mới
     *
     * @return CreateBookingResponse chứa thông tin booking vừa được tạo
     *
     * @author Phong
     * @version 1.0
     */
    CreateBookingResponse createBooking(CreateBookingRequest request);
}
