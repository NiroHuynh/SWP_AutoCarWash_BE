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
import com.swp.autocarwash.booking.calculator.SlotAvailabilityCalculator;
import com.swp.autocarwash.booking.dto.request.CreateBookingRequest;
import com.swp.autocarwash.booking.dto.response.CreateBookingResponse;
import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.BookingSlot;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.port.AddonServicePort;
import com.swp.autocarwash.booking.port.ServicePackagePort;
import com.swp.autocarwash.booking.port.VehiclePort;
import com.swp.autocarwash.booking.port.VoucherPort;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.repository.BookingSlotRepository;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


import java.time.LocalDate;
import java.util.List;

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
    private final BookingSlotRepository slotRepository;
    private final ServicePackagePort servicePackagePort;
    private final AddonServicePort addonServicePort;
    private final VoucherPort voucherPort;
    private final VehiclePort vehiclePort;

    private final ModelMapper modelMapper;
    private final SlotAvailabilityCalculator slotCalculator = new SlotAvailabilityCalculator();
    /**
     * {@inheritDoc}
     *
     * <p>Luồng xử lý:
     * <ol>
     *   <li>Truy vấn booking có trạng thái UPCOMING.</li>
     *   <li>Với mỗi booking, lấy slot đầu (startTime) và slot cuối (endTime).</li>
     *   <li>Xác định allowedActions theo trạng thái và thời gian còn lại.</li>
     *   <li>Ánh xạ sang DTO, sắp xếp ASC theo ngày hẹn rồi startTime nếu cùng ngày.</li>
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
                customerId, UPCOMING_STATUSES);

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
     *   <li>Truy vấn booking có trạng thái PAST.</li>
     *   <li>Với mỗi booking, lấy slot đầu (startTime) và slot cuối (endTime).</li>
     *   <li>Tính allowedActions theo trạng thái.</li>
     *   <li>Ánh xạ sang {@link BookingCardResponse}, sắp xếp DESC theo
     *       ngày hẹn rồi {@code startTime}, và trả về.</li>
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
                customerId, PAST_STATUSES);

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
                .sorted(Comparator
                        .comparing(BookingCardResponse::getAppointmentDate, Comparator.reverseOrder())
                        .thenComparing(Comparator.comparing(
                                BookingCardResponse::getStartTime,
                                Comparator.nullsLast(Comparator.reverseOrder()))))
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
     * {@inheritDoc}
     *
     * <p>Luồng xử lý:
     * <ol>
     *   <li>Tìm booking theo ID (404 nếu không tồn tại).</li>
     *   <li>Cập nhật status = CANCELLED, canceledAt = now, lưu booking.</li>
     *   <li>Xóa các BookingSlotAllocation gắn với booking để giải phóng slot.</li>
     *   <li>Trả về BookingDetailResponse phản ánh trạng thái mới.</li>
     * </ol>
     * </p>
     *
     * @param bookingId mã định danh của lịch đặt cần hủy
     * @return chi tiết booking sau khi hủy
     * @throws ResourceNotFoundException nếu không tìm thấy booking
     */
    @Override
    @Transactional
    public BookingDetailResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findDetailById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND));

        booking.setStatus("CANCELLED");
        booking.setCanceledAt(Instant.now());
        bookingRepository.save(booking);

        bookingSlotAllocationRepository.deleteByBookingId(bookingId);

        return getBookingDetail(bookingId);
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
   @Override
    @Transactional
    public CreateBookingResponse createBooking(CreateBookingRequest request) {

        // 1. VALIDATE VEHICLE
        VehicleContract vehicle = vehiclePort.getById(request.getVehicleId());
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.VEHICLE_NOT_FOUND);
        }

        // 2. SERVICE PACKAGE
        var servicePackage = servicePackagePort.getById(request.getServicePackageId());

        // 3. ADDONS
        List<AddonServiceContract> addons = addonServicePort.getByIds(request.getAddonServiceIds());

        // 4. VALIDATE SLOTS (REAL BUSINESS RULE)
        List<BookingSlot> slots = slotRepository.findByIdIn(request.getSlotIds());

        if (slots.size() != request.getSlotIds().size()) {
            throw new BusinessException(ErrorCode.BOOKING_SLOT_NOT_AVAILABLE);
        }

        // check cùng station + liên tục + capacity
        boolean valid = slotCalculator.validateContinuousSlots(slots, servicePackage.getDurationMinutes());

        if (!valid) {
            throw new BusinessException(ErrorCode.BOOKING_INVALID_SLOT);
        }

        // 5. PRICE CALCULATION
        BigDecimal packagePrice = servicePackage.getBasePrice();

        BigDecimal addonPrice = addons.stream()
                .map(AddonServiceContract::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal subTotal = packagePrice.add(addonPrice);

        // 6. VOUCHER
        BigDecimal discount = BigDecimal.ZERO;

        if (request.getVoucherCode() != null) {

            var voucher = voucherPort.getDiscountPercent(request.getVoucherCode(), subTotal.intValue());

            if (!voucher.isValid()) {
                throw new BusinessException(ErrorCode.VOUCHER_INVALID);
            }

            discount = subTotal.multiply(
                    BigDecimal.valueOf(voucher.getDiscountPercentage())
                            .divide(BigDecimal.valueOf(100))
            );

            subTotal = subTotal.subtract(discount);
        }

        // 7. BUILD BOOKING ENTITY (FIXED)
        Booking booking = new Booking();
        booking.setVehicle(modelMapper.map(vehicle, Vehicle.class));
        booking.setServicePackage(modelMapper.map(servicePackage, ServicePackage.class));
        booking.setAppointmentDate(LocalDate.parse(request.getAppointmentDate()));
        booking.setStatus(BookingStatus.PENDING.toString());
        booking.setBookingType("ONLINE");
        booking.setTotalServiceAmount(packagePrice);
        booking.setTotalAddonAmount(addonPrice);
        booking.setVoucherDiscountAmount(discount);
        booking.setTotalAmount(subTotal);

        Booking saved = bookingRepository.save(booking);

        // 8. SLOT ALLOCATION (MISSING BEFORE)
        // TODO: insert booking_slot_allocation

        return CreateBookingResponse.builder()
                .bookingId(saved.getId())
                .status(saved.getStatus())
                .totalAmount(subTotal)
                .slotIds(request.getSlotIds())
                .build();
    }

}