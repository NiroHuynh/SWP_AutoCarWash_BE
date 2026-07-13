package com.swp.autocarwash.booking.service;

import com.swp.autocarwash.booking.dto.response.BookingCardResponse;
import com.swp.autocarwash.booking.dto.response.BookingDetailResponse;
import com.swp.autocarwash.booking.dto.request.CreateBookingRequest;
import com.swp.autocarwash.booking.dto.response.CreateBookingResponse;
import com.swp.autocarwash.booking.dto.response.StationBookingListPageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
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
     * <p>Chỉ trả về các booking có trạng thái {@code CONFIRMED}, {@code CHECK_IN}
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
     * <p>Chỉ trả về các booking có trạng thái {@code PAID}, {@code CANCELED}
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
     * <p>Chuyển status booking sang {@code CANCELED}, ghi nhận
     * {@code canceledAt}, và xóa các {@code BookingSlotAllocation} liên quan
     * để giải phóng slot cho khách hàng khác.</p>
     *
     * @param bookingId mã định danh của lịch đặt cần hủy
     * @return {@link BookingDetailResponse} của booking sau khi hủy (status = CANCELED)
     * @throws com.swp.autocarwash.common.exception.ResourceNotFoundException
     *         nếu không tìm thấy booking với ID đã cho
     */
    BookingDetailResponse cancelBooking(Long bookingId);


    /**
     * Chức năng: Staff hủy 1 booking đang CHECK_IN trong queue vì khách bỏ về (BL-QU-05).
     *
     * <p>Khác với {@link #cancelBooking(Long)} (khách tự hủy khi còn CONFIRMED):
     * action này chỉ staff thực hiện, chỉ áp dụng khi booking đã CHECK_IN, và
     * phát sinh hậu quả khác hẳn (tịch thu cọc / cộng điểm vi phạm / khóa tài khoản-xe).</p>
     *
     * @param bookingId    id của booking đang CHECK_IN cần hủy
     * @param actingUserId id của user (auth.User) đang đăng nhập, để xác định Staff thực hiện hành động
     * @return thông tin chi tiết booking sau khi hủy
     * @author KimNgan
     * @version 1.0
     */
    BookingDetailResponse cancelGuestLeftAtCheckIn(Long bookingId, Long actingUserId);
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

    /**
     * Chức năng: Staff/Admin xem danh sách toàn bộ booking của 1 chi nhánh cụ
     * thể, filter theo trạng thái/khoảng ngày/từ khóa (tên khách, SĐT, biển số).
     *
     * @param stationId chi nhánh cần xem (staff bị pin cứng về station của mình)
     * @param status    giá trị thô của BookingStatus, bỏ trống = không lọc
     * @param fromDate  lọc appointmentDate từ ngày này trở đi, bỏ trống = không giới hạn
     * @param toDate    lọc appointmentDate đến ngày này, bỏ trống = không giới hạn
     * @param keyword   tìm theo tên khách/SĐT/biển số, bỏ trống = không lọc
     * @author Ngân
     * @version 1.0
     */
    StationBookingListPageResponse getStationBookingList(
            Integer stationId, String status, LocalDate fromDate, LocalDate toDate,
            String keyword, Pageable pageable);
}
