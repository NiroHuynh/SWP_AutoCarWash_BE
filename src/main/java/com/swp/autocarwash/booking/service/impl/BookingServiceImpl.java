package com.swp.autocarwash.booking.service.impl;

import com.swp.autocarwash.booking.dto.response.BookingCardResponse;
import com.swp.autocarwash.booking.dto.response.BookingDetailResponse;
import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.BookingAddon;
import com.swp.autocarwash.booking.entity.BookingSlotAllocation;
import com.swp.autocarwash.booking.mapper.BookingHistoryMapper;
import com.swp.autocarwash.booking.repository.BookingAddonRepository;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.repository.BookingSlotAllocationRepository;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.promotion.entity.VoucherUsage;
import com.swp.autocarwash.promotion.repository.VoucherUsageRepository;
import com.swp.autocarwash.station.entity.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Triển khai các nghiệp vụ lịch đặt xe định nghĩa trong {@link BookingService}.
 *
 * <p>Phối hợp giữa {@link BookingRepository} để truy vấn dữ liệu,
 * {@link BookingSlotAllocationRepository} để lấy thông tin khung giờ,
 * và {@link BookingHistoryMapper} để chuyển đổi sang DTO phản hồi.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    /** Danh sách trạng thái được coi là "sắp tới" theo AC-25.1.2. */
    private static final List<String> UPCOMING_STATUSES =
            List.of("CONFIRMED", "CHECKED_IN", "WASHING");

    /** Danh sách trạng thái lịch sử theo AC-25.2.1. */
    private static final List<String> PAST_STATUSES =
            List.of("PAID", "CANCELLED", "NO_SHOW");

    /** Ngưỡng thời gian (phút) để hiển thị nút CANCEL theo AC-25.1.5. */
    private static final long CANCEL_THRESHOLD_MINUTES = 120;

    /** Múi giờ hệ thống. */
    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final BookingRepository bookingRepository;
    private final BookingSlotAllocationRepository bookingSlotAllocationRepository;
    private final BookingAddonRepository bookingAddonRepository;
    private final VoucherUsageRepository voucherUsageRepository;
    private final BookingHistoryMapper bookingHistoryMapper;

    /**
     * {@inheritDoc}
     *
     * <p>Luồng xử lý:
     * <ol>
     *   <li>Truy vấn booking có trạng thái UPCOMING, sắp xếp ASC theo ngày hẹn.</li>
     *   <li>Với mỗi booking, lấy slot đầu (startTime) và slot cuối (endTime).</li>
     *   <li>Xác định allowedActions theo trạng thái và thời gian còn lại.</li>
     *   <li>Sắp xếp thêm theo startTime nếu cùng ngày, rồi ánh xạ sang DTO.</li>
     * </ol>
     * </p>
     *
     * @param customerId mã định danh của khách hàng đang đăng nhập
     * @return danh sách booking sắp tới đã sắp xếp, hoặc danh sách rỗng nếu không có
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingCardResponse> getUpcomingBookings(Long customerId) {
        List<Booking> bookings = bookingRepository.findByCustomerIdAndStatuses(
                customerId, UPCOMING_STATUSES, Sort.by(Sort.Direction.ASC, "appointmentDate"));

        return bookings.stream()
                .map(booking -> {
                    List<BookingSlotAllocation> allocations =
                            bookingSlotAllocationRepository.findByBookingId(booking.getId());

                    LocalTime startTime = allocations.isEmpty()
                            ? null
                            : allocations.get(0).getBookingSlot().getStartTime();

                    LocalTime endTime = allocations.isEmpty()
                            ? null
                            : allocations.get(allocations.size() - 1).getBookingSlot().getEndTime();

                    List<String> allowedActions = determineAllowedActions(booking, startTime);

                    return bookingHistoryMapper.toBookingCardResponse(
                            booking, startTime, endTime, allowedActions);
                })
                .sorted(Comparator
                        .comparing(BookingCardResponse::getAppointmentDate)
                        .thenComparing(Comparator.comparing(
                                BookingCardResponse::getStartTime,
                                Comparator.nullsLast(Comparator.naturalOrder()))))
                .toList();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Luồng xử lý:
     * <ol>
     *   <li>Truy vấn booking có trạng thái PAST, sắp xếp DESC theo ngày hẹn.</li>
     *   <li>Với mỗi booking, lấy slot đầu (startTime) và slot cuối (endTime).</li>
     *   <li>Tính allowedActions theo trạng thái.</li>
     *   <li>Ánh xạ sang {@link BookingCardResponse} và trả về.</li>
     * </ol>
     * </p>
     *
     * @param customerId mã định danh của khách hàng đang đăng nhập
     * @return danh sách lịch sử dịch vụ mới nhất lên đầu, hoặc danh sách rỗng nếu không có
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingCardResponse> getPastBookings(Long customerId) {
        List<Booking> bookings = bookingRepository.findByCustomerIdAndStatuses(
                customerId, PAST_STATUSES, Sort.by(Sort.Direction.DESC, "appointmentDate"));

        return bookings.stream()
                .map(booking -> {
                    List<BookingSlotAllocation> allocations =
                            bookingSlotAllocationRepository.findByBookingId(booking.getId());

                    LocalTime startTime = allocations.isEmpty()
                            ? null
                            : allocations.get(0).getBookingSlot().getStartTime();

                    LocalTime endTime = allocations.isEmpty()
                            ? null
                            : allocations.get(allocations.size() - 1).getBookingSlot().getEndTime();

                    return bookingHistoryMapper.toBookingCardResponse(
                            booking,
                            startTime,
                            endTime,
                            determineAllowedActions(booking, startTime));
                })
                .toList();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Luồng xử lý:
     * <ol>
     *   <li>Tìm booking theo ID, eager-fetch vehicle, servicePackage, checkInEmployee.</li>
     *   <li>Lấy allocations (với slot + station) để xác định startTime, endTime, station.</li>
     *   <li>Lấy danh sách addon và voucher đã dùng.</li>
     *   <li>Tính remainingAmount = totalAmount - depositAmount.</li>
     *   <li>Ánh xạ sang {@link BookingDetailResponse} và trả về.</li>
     * </ol>
     * </p>
     *
     * @param bookingId mã định danh của lịch đặt cần xem
     * @return chi tiết đầy đủ của lịch đặt
     * @throws ResourceNotFoundException nếu không tìm thấy booking
     */
    @Override
    @Transactional(readOnly = true)
    public BookingDetailResponse getBookingDetail(Long bookingId) {
        Booking booking = bookingRepository.findDetailById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND));

        List<BookingSlotAllocation> allocations =
                bookingSlotAllocationRepository.findByBookingId(bookingId);
        List<BookingAddon> addons =
                bookingAddonRepository.findByBookingId(bookingId);
        Optional<VoucherUsage> voucherUsage =
                voucherUsageRepository.findUsedByBookingId(bookingId);

        LocalTime startTime = allocations.isEmpty()
                ? null : allocations.get(0).getBookingSlot().getStartTime();
        LocalTime endTime = allocations.isEmpty()
                ? null : allocations.get(allocations.size() - 1).getBookingSlot().getEndTime();
        Station station = allocations.isEmpty()
                ? null : allocations.get(0).getBookingSlot().getStation();

        String technicianName = booking.getCheckInEmployee() != null
                ? booking.getCheckInEmployee().getFirstName()
                  + " " + booking.getCheckInEmployee().getLastName()
                : null;

        String voucherCode = voucherUsage
                .map(vu -> vu.getVoucher().getVoucherCode()).orElse(null);
        Integer voucherDiscountPercent = voucherUsage
                .map(vu -> vu.getVoucher().getDiscountPercentage()).orElse(null);

        BigDecimal deposit = booking.getDepositAmount() != null
                ? booking.getDepositAmount() : BigDecimal.ZERO;
        BigDecimal remainingAmount = booking.getTotalAmount().subtract(deposit);

        String status = booking.getStatus();

        return bookingHistoryMapper.toBookingDetailResponse(
                booking, startTime, endTime, station, addons,
               // mapStatusLabel(status), mapStatusColor(status),
                technicianName, voucherCode, voucherDiscountPercent, remainingAmount);
    }

    /**
     * Xác định danh sách hành động được phép trên một booking card, áp dụng cho cả
     * tab Upcoming (AC-25.1.4 / AC-25.1.5 / AC-25.1.6) và Past Services (AC-25.2.3).
     *
     * @param booking   entity lịch đặt cần kiểm tra trạng thái
     * @param startTime giờ bắt đầu slot; dùng {@code 00:00} làm fallback nếu chưa có slot
     * @return danh sách tên hành động được phép
     */
    private List<String> determineAllowedActions(Booking booking, LocalTime startTime) {
        return switch (booking.getStatus()) {
            case "PAID"                  -> List.of("WRITE_REVIEW");
            case "CANCELLED", "NO_SHOW" -> List.of();
            case "CHECKED_IN", "WASHING" -> List.of("VIEW_DETAILS");
            case "CONFIRMED" -> {
                LocalTime effectiveStart = startTime != null ? startTime : LocalTime.MIDNIGHT;
                LocalDateTime appointmentDateTime =
                        LocalDateTime.of(booking.getAppointmentDate(), effectiveStart);
                long minutesUntil = ChronoUnit.MINUTES.between(LocalDateTime.now(ZONE), appointmentDateTime);
                yield minutesUntil >= CANCEL_THRESHOLD_MINUTES
                        ? List.of("CANCEL", "VIEW_DETAILS")
                        : List.of("VIEW_DETAILS");
            }
            default -> List.of("VIEW_DETAILS");
        };
    }

//    /**
//     * Trả về nhãn trạng thái hiển thị cho người dùng theo AC-25.2.4.
//     *
//     * @param status trạng thái nội bộ của booking
//     * @return chuỗi nhãn tiếng Việt tương ứng
//     */
//    private String mapStatusLabel(String status) {
//        return switch (status) {
//            case "CONFIRMED"  -> "Đã xác nhận";
//            case "CHECKED_IN" -> "Đã check-in";
//            case "WASHING"    -> "Đang rửa xe";
//            case "PAID"       -> "Đã thanh toán";
//            case "CANCELLED"  -> "Đã hủy";
//            case "NO_SHOW"    -> "Vắng mặt";
//            default           -> status;
//        };
//    }
//
//    /**
//     * Trả về mã màu hex cho badge trạng thái (dùng cho cả Upcoming, Past và Detail).
//     *
//     * @param status trạng thái nội bộ của booking
//     * @return mã màu hex (ví dụ: "#22C55E")
//     */
//    private String mapStatusColor(String status) {
//        return switch (status) {
//            case "CONFIRMED"  -> "#3B82F6";
//            case "CHECKED_IN" -> "#EAB308";
//            case "WASHING"    -> "#14B8A6";
//            case "PAID"       -> "#22C55E";
//            case "CANCELLED"  -> "#9CA3AF";
//            case "NO_SHOW"    -> "#F97316";
//            default           -> "#6B7280";
//        };
//    }
}
