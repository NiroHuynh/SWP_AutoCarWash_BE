package com.swp.autocarwash.booking.service.impl;

import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.booking.dto.response.BookingCardResponse;
import com.swp.autocarwash.booking.dto.response.BookingDetailResponse;
import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.BookingAddon;
import com.swp.autocarwash.booking.entity.*;
import com.swp.autocarwash.booking.entity.enums.BookingType;
import com.swp.autocarwash.booking.event.BookingCanceledEvent;
import com.swp.autocarwash.booking.event.BookingEventPublisher;
import com.swp.autocarwash.booking.entity.BookingSlotAllocation;
import com.swp.autocarwash.booking.mapper.BookingHistoryMapper;
import com.swp.autocarwash.booking.port.*;
import com.swp.autocarwash.booking.repository.BookingAddonRepository;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.repository.BookingSlotAllocationRepository;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.booking.validator.BookingValidator;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.booking.mapper.BookingHistoryMapper.SubscriptionInfo;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.promotion.entity.VoucherUsage;
import com.swp.autocarwash.promotion.repository.VoucherUsageRepository;
import com.swp.autocarwash.queue.entity.enums
        .QueueStatus;
import com.swp.autocarwash.queue.repository.custom.QueueTicketRepository;
import com.swp.autocarwash.servicepackage.entity.AddonService;
import com.swp.autocarwash.station.entity.Station;
import com.swp.autocarwash.subscription.repository.FamilySubscriptionRepository;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import com.swp.autocarwash.booking.calculator.SlotAvailabilityCalculator;
import com.swp.autocarwash.booking.dto.request.CreateBookingRequest;
import com.swp.autocarwash.booking.dto.response.CreateBookingResponse;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.port.AddonServicePort;
import com.swp.autocarwash.booking.port.ServicePackagePort;
import com.swp.autocarwash.booking.port.VehiclePort;
import com.swp.autocarwash.booking.port.VoucherPort;
import com.swp.autocarwash.booking.repository.BookingSlotRepository;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;


import java.time.LocalDate;


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
// là một annotation từ thư viện Lombok — tự động sinh ra constructor cho các field final
public class BookingServiceImpl implements BookingService {


    /**
     * Danh sách trạng thái được coi là "sắp tới" theo AC-25.1.2.
     */
    private static final List<String> UPCOMING_STATUSES =
            List.of(BookingStatus.CONFIRMED.name(),
                    BookingStatus.CHECK_IN.name(),
                    BookingStatus.WASHING.name(),
                    BookingStatus.COMPLETED.name(),
                    BookingStatus.PENDING.name());

    /**
     * Danh sách trạng thái lịch sử theo AC-25.2.1.
     */
    private static final List<String> PAST_STATUSES =
            List.of(BookingStatus.CHECK_OUT.name(),
                    BookingStatus.CANCELED.name(),
                    BookingStatus.NO_SHOW.name());

    /**
     * Ngưỡng thời gian (phút) để hiển thị nút CANCEL theo AC-25.1.5.
     */
    private static final long CANCEL_THRESHOLD_MINUTES = 120;

    /**
     * Số tiền đặt cọc cố định theo BL-BK-00 (không lưu theo từng booking).
     */
    private static final BigDecimal DEFAULT_DEPOSIT_AMOUNT = BigDecimal.valueOf(20000);

    /**
     * Múi giờ hệ thống.
     * Múi giờ Việt Nam (UTC+7) — dùng thay cho LocalDateTime.now() để đảm bảo
     * đúng giờ VN khi deploy lên server nước ngoài (thường chạy UTC)
     */
    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    /**
     * Publisher riêng của module booking, bọc lại ApplicationEventPublisher của Spring.
     * Khi gọi publishBookingCanceled(), Spring sẽ tìm tất cả class có annotation
     *
     * @EventListener/@TransactionalEventListener lắng nghe BookingCanceledEvent để thông báo
     * "có 1 sự kiện vừa diễn ra" — còn làm gì sau khi nghe thông báo thì tự đám listener xử lí.
     */
    private final BookingEventPublisher bookingEventPublisher;

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
    private final FamilySubscriptionPort familySubscriptionPort;
    private final UnlimitSubscriptionPort unlimitSubscriptionPort;
    private final BookingValidator bookingValidator;
    private final CustomerPort customerPort;
    private final SecurityUtils securityUtils;
    private final VoucherUsagePort voucherUsagePort;
    private final BookingInvoicePort bookingInvoicePort;
    private final LoyaltyPort loyaltyPort;
    private final SystemSettingPort systemSettingPort;
    private final PaymentQrPort paymentQrPort;

    private final ModelMapper modelMapper;
    private final SlotAvailabilityCalculator slotCalculator = new SlotAvailabilityCalculator();
    private final QueueTicketRepository queueTicketRepository;
    private final UnlimitSubscriptionRepository unlimitSubscriptionRepository;
    private final FamilySubscriptionRepository familySubscriptionRepository;
    private final BookingSlotRepository bookingSlotRepository;


    /**
     * Xây dựng danh sách {@link BookingCardResponse} từ danh sách booking, dùng chung
     * cho cả {@link #getUpcomingBookings(Long)} và {@link #getPastBookings(Long)}.
     *
     * <p>Với mỗi booking: lấy slot đầu/cuối để xác định startTime/endTime, tính
     * allowedActions theo trạng thái, rồi map sang {@link BookingCardResponse}.
     * Kết quả trả về chưa được sắp xếp — việc sắp xếp do phương thức gọi đảm nhiệm.</p>
     *
     * @param bookings danh sách booking cần chuyển đổi
     * @return danh sách {@link BookingCardResponse} chưa sắp xếp tương ứng với {@code bookings};
     * danh sách rỗng nếu {@code bookings} rỗng
     */
    private List<BookingCardResponse> buildBookingCardResponses(List<Booking> bookings) {
        List<BookingCardResponse> result = new ArrayList<>();

        for (Booking b : bookings) {
            // Lấy danh sách slot của booking
            List<BookingSlotAllocation> allocation =
                    bookingSlotAllocationRepository.findByBookingId(b.getId());

            // Lấy startTime và endTime
            LocalTime startTime = null;
            LocalTime endTime = null;

            if (!allocation.isEmpty()) {
                startTime = allocation.getFirst().getBookingSlot().getStartTime();
                endTime = allocation.getLast().getBookingSlot().getEndTime();
            }

            // Xác định hành động được phép
            List<String> allowedAction = determineAllowedActions(b, startTime);

            // Map sang response
            BookingCardResponse bookingResponse = bookingHistoryMapper
                    .toBookingCardResponse(b, startTime, endTime, allowedAction);

            result.add(bookingResponse);
        }

        return result;
    }


    /**
     * {@inheritDoc}
     *
     * <p>Luồng xử lý:
     * <ol>
     *   <li>Truy vấn booking có trạng thái nằm trong {@code UPCOMING_STATUSES}.</li>
     *   <li>Gọi {@link #buildBookingCardResponses(List)} để map sang danh sách card.</li>
     *   <li>Sắp xếp ASC theo ngày hẹn, rồi theo giờ bắt đầu (booking chưa có slot xếp cuối).</li>
     * </ol>
     * </p>
     *
     * @param customerId mã định danh của khách hàng đang đăng nhập
     * @return danh sách booking sắp tới đã sắp xếp, hoặc danh sách rỗng nếu không có
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingCardResponse> getUpcomingBookings(Long customerId) {
        // Phần riêng: lấy đúng danh sách status
        List<Booking> bookings = bookingRepository
                .findByCustomerIdAndStatuses(customerId, UPCOMING_STATUSES);

        // Phần chung: build response
        List<BookingCardResponse> result = buildBookingCardResponses(bookings);

        // Phần riêng: sort ASC (gần nhất lên đầu)
        result.sort(Comparator
                .comparing(BookingCardResponse::getAppointmentDate)
                .thenComparing(
                        BookingCardResponse::getStartTime,
                        Comparator.nullsLast(Comparator.naturalOrder())));

        return result;
    }


    /**
     * {@inheritDoc}
     *
     * <p>Luồng xử lý:
     * <ol>
     *   <li>Truy vấn booking có trạng thái nằm trong {@code PAST_STATUSES}.</li>
     *   <li>Gọi {@link #buildBookingCardResponses(List)} để map sang danh sách card.</li>
     *   <li>Sắp xếp DESC theo ngày hẹn, rồi theo giờ bắt đầu (booking chưa có slot xếp cuối).</li>
     * </ol>
     * </p>
     *
     * @param customerId mã định danh của khách hàng đang đăng nhập
     * @return danh sách lịch sử dịch vụ mới nhất lên đầu, hoặc danh sách rỗng nếu không có
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingCardResponse> getPastBookings(Long customerId) {
        // Phần riêng: lấy đúng danh sách status
        List<Booking> bookings = bookingRepository
                .findByCustomerIdAndStatuses(customerId, PAST_STATUSES);

        // Phần chung: build response
        List<BookingCardResponse> result = buildBookingCardResponses(bookings);

        // Phần riêng: sort DESC (mới nhất lên đầu)
        result.sort(Comparator
                .comparing(BookingCardResponse::getAppointmentDate, Comparator.reverseOrder())
                .thenComparing(
                        BookingCardResponse::getStartTime,
                        Comparator.nullsLast(Comparator.reverseOrder())));

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Luồng xử lý:
     * <ol>
     *   <li>Tìm booking theo ID, ném lỗi 404 nếu không tồn tại.</li>
     *   <li>Lấy allocations (slot + station), addons và voucher đã dùng của booking.</li>
     *   <li>Xác định startTime, endTime, station từ slot đầu/cuối (nếu có).</li>
     *   <li>Lấy tên kỹ thuật viên check-in (nếu đã check-in) và thông tin voucher (nếu có).</li>
     *   <li>Tính remainingAmount = totalAmount - tiền cọc cố định (nếu đã đặt cọc).</li>
     *   <li>Map toàn bộ dữ liệu sang {@link BookingDetailResponse} và trả về.</li>
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

        // Bước 1: Tìm booking theo ID, không có thì báo lỗi
        Booking booking = bookingRepository.findDetailById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND));

        // Bước 2: Lấy các dữ liệu liên quan
        List<BookingSlotAllocation> allocations =
                bookingSlotAllocationRepository.findByBookingId(bookingId);
        List<BookingAddon> addons =
                bookingAddonRepository.findByBookingId(bookingId);
        Optional<VoucherUsage> voucherUsage =
                voucherUsageRepository.findUsedByBookingId(bookingId);

        // Bước 3: Lấy startTime, endTime, station từ allocations
        LocalTime startTime = null;
        LocalTime endTime = null;
        Station station = null;

        if (!allocations.isEmpty()) {
            startTime = allocations.getFirst().getBookingSlot().getStartTime();
            endTime = allocations.getLast().getBookingSlot().getEndTime();
            station = allocations.getFirst().getBookingSlot().getStation();
        }

        // Bước 4: Lấy tên kỹ thuật viên (có thể chưa có nếu chưa check-in)
        String technicianName = null;

        if (booking.getCheckInEmployee() != null) {
            technicianName = booking.getCheckInEmployee().getFirstName()
                    + " " + booking.getCheckInEmployee().getLastName();
        }

        // Bước 5: Lấy thông tin voucher (booking có thể không dùng voucher)
        String voucherCode = null;
        Integer voucherDiscountPercent = null;

        if (voucherUsage.isPresent()) {
            voucherCode = voucherUsage.get().getVoucher().getVoucherCode();
            voucherDiscountPercent = voucherUsage.get().getVoucher().getDiscountPercentage();
        }

        // Bước 6: Tính số tiền còn lại = tổng tiền - tiền cọc
        BigDecimal deposit = Boolean.TRUE.equals(booking.getIsDepositPaid())
                ? DEFAULT_DEPOSIT_AMOUNT
                : BigDecimal.ZERO;

        BigDecimal remainingAmount = booking.getTotalAmount().subtract(deposit);

        // Bước 6.5: Lấy thông tin subscription plan nếu customer có gói ACTIVE cho xe này
        SubscriptionInfo subscriptionInfo = null;
        if (booking.getCustomer() != null) {
            Long cId = booking.getCustomer().getId();
            Long vId = booking.getVehicle().getId();
            subscriptionInfo = unlimitSubscriptionRepository
                    .findActiveByCustomerAndVehicle(cId, vId)
                    .map(u -> new SubscriptionInfo(
                            u.getSubscriptionPlan().getPlanName(),
                            u.getSubscriptionPlan().getPlanType(),
                            u.getSubscriptionPlan().getDurationDays()))
                    .orElseGet(() -> familySubscriptionRepository
                            .findActiveByCustomerAndVehicle(cId, vId)
                            .map(f -> new SubscriptionInfo(
                                    f.getSubscriptionPlan().getPlanName(),
                                    f.getSubscriptionPlan().getPlanType(),
                                    f.getSubscriptionPlan().getDurationDays()))
                            .orElse(null));
        }




        // Bước 6.6: Điểm loyalty — chỉ chốt & hiển thị khi booking đã CHECK_OUT
        // (COMPLETED = rửa xong nhưng chưa thanh toán nên chưa phát sinh điểm).
        // Đồng thời tránh NPE khi booking là walk-in (customer == null).
        Integer loyaltyPoint = booking.getCustomer() != null
                ? loyaltyPort.getLotaltyPoint(booking.getCustomer().getId())
                : null;
        Integer pointsEarned = null;
        Integer pointsRedeemed = null;
        if (BookingStatus.CHECK_OUT.name().equals(booking.getStatus())
                && booking.getCustomer() != null) {
            pointsEarned = loyaltyPort.getEarnedPointForBooking(bookingId);
            pointsRedeemed = loyaltyPort.getRedeemedPointForBooking(bookingId);
        }


        // Bước 7: Map tất cả dữ liệu sang response rồi trả về
        return bookingHistoryMapper.toBookingDetailResponse(
                booking, startTime, endTime, station, addons,
                technicianName, voucherCode, voucherDiscountPercent, deposit, remainingAmount,
                subscriptionInfo, loyaltyPoint, pointsEarned, pointsRedeemed);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Luồng xử lý:
     * <ol>
     *   <li>Tìm booking theo ID (404 nếu không tồn tại).</li>
     *   <li>Cập nhật status = CANCELED, canceledAt = now, lưu booking.</li>
     *   <li>Giảm bookedCount của từng slot đã cấp cho booking để giải phóng chỗ;
     *       giữ nguyên BookingSlotAllocation để vẫn hiển thị được giờ đã đặt trên
     *       booking card/detail sau khi hủy.</li>
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

        // FE-47-US-02 AC01/AC03: chỉ hoàn voucher khi hủy hợp lệ — booking đang CONFIRMED
        // (chưa check-in) VÀ do chính customer bấm hủy (không phải staff hủy giúp).
        boolean isValidCancellation = BookingStatus.CONFIRMED.name().equals(booking.getStatus())
                && securityUtils.isCurrentUserCustomer();

        booking.setStatus(BookingStatus.CANCELED.name());
        booking.setCanceledAt(LocalDateTime.now());
        bookingRepository.save(booking);

        if (isValidCancellation) {
            voucherUsagePort.releaseVoucher(bookingId);
        }

        List<BookingSlotAllocation> allocations =
                bookingSlotAllocationRepository.findByBookingId(bookingId);
        for (BookingSlotAllocation allocation : allocations) {
            BookingSlot slot = allocation.getBookingSlot();
            slot.setBookedCount(slot.getBookedCount() - 1);
            slotRepository.save(slot);
        }
        // Chỉ bắn BookingCanceledEvent khi xe đã check-in trước đó (checkInEmployee != null) —
        // còn CONFIRMED (chưa check-in) không phát sinh event này, vì khách cancel trước khi tới tiệm => employess = null

        if (booking.getCheckInEmployee() != null) {
            bookingEventPublisher.publishBookingCanceled(BookingCanceledEvent.builder()
                    .customerId(booking.getCustomer() != null ? booking.getCustomer().getId() : null)
                    .vehicleId(booking.getVehicle().getId())
                    .bookingId(bookingId)
                    .canceledByStaffId(null)
                    .bookingType(booking.getBookingType())
                    .isDepositPaid(booking.getIsDepositPaid())
                    .checkInAt(Instant.parse(booking.getCheckInAt().toString()))
                    .canceledAt(Instant.parse(booking.getCanceledAt().toString()))
                    .build());
        }

        return getBookingDetail(bookingId);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Luồng xử lý:
     * <ol>
     *   <li>Validate booking đang ở trạng thái CHECK_IN — không cho hủy nếu chưa/đã qua trạng thái này.</li>
     *   <li>Đổi status booking sang CANCELED, ghi nhận canceledAt.</li>
     *   <li>Giải phóng slot đã đặt (giảm bookedCount).</li>
     *   <li>Đồng bộ QueueTicket tương ứng sang CANCELED .</li>
     *   <li>Resolve Staff thực hiện hành động từ actingUserId, publish BookingCanceledEvent
     *       để các listener xử lý tịch thu cọc / cộng điểm vi phạm / cập nhật dashboard.</li>
     * </ol>
     * </p>
     */

    @Override
    @Transactional
    public BookingDetailResponse cancelGuestLeftAtCheckIn(Long bookingId, Long actingUserId) {
        Booking booking = bookingRepository.findDetailById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND));

        if(!BookingStatus.CHECK_IN.name().equals(booking.getStatus())){
            throw new BusinessException(ErrorCode. BOOKING_NOT_CHECKED_IN);
        }

        booking.setStatus(BookingStatus.CANCELED.name());
        booking.setCanceledAt(LocalDateTime.now(ZONE));
        // AC02: booking single-package đã trả cọc online -> hủy do khách bỏ về => KHÔNG hoàn cọc.
        // "Thu 100% cọc" chỉ là ghi nhận tịch thu (không refund). Gói không cọc (isDepositPaid=false)
        // giữ nguyên depositConfiscatedAt = null. Mirror DepositConfiscationScheduler.
        if (Boolean.TRUE.equals(booking.getIsDepositPaid())) {
            booking.setDepositConfiscatedAt(LocalDateTime.now(ZONE));
        }
        bookingRepository.save(booking);

        List<BookingSlotAllocation> allocations =
                bookingSlotAllocationRepository.findByBookingId(bookingId);
        for (BookingSlotAllocation allocation : allocations) {
            BookingSlot slot = allocation.getBookingSlot();
            slot.setBookedCount(slot.getBookedCount() - 1);
            slotRepository.save(slot);
        }
        // Chỉ bắn BookingCanceledEvent khi xe đã check-in trước đó (checkInEmployee != null) —
        // còn CONFIRMED (chưa check-in) không phát sinh event này, vì khách cancel trước khi tới tiệm => employess = null
        queueTicketRepository.findQueueTicketByBookingId(bookingId).ifPresent(ticket -> {
            ticket.setStatus(QueueStatus.CANCELED.name());
            queueTicketRepository.save(ticket);
        });

            bookingEventPublisher.publishBookingCanceled(BookingCanceledEvent.builder()
                    .customerId(booking.getCustomer() != null ? booking.getCustomer().getId() : null)
                    .canceledByStaffId(actingUserId) // id chỗ này lấy theo userId chứ ko lấy theo id staff vì 2 id này khác nhau nên để đơn giản thì lấy userId, nếu sau này muốn dùng các thông tin khác của staff thì có thể join vào bảng staff
                    .vehicleId((long) booking.getVehicle().getId().intValue())
                    .bookingId(bookingId)
                    .bookingType(booking.getBookingType())
                    .isDepositPaid(booking.getIsDepositPaid())
                    .checkInAt(booking.getCheckInAt().atZone(ZONE).toInstant())
                    .canceledAt(booking.getCanceledAt().atZone(ZONE).toInstant()).build());


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
        List<String> actions;
        switch (booking.getStatus()) {
            case "CHECK_OUT":
                actions = List.of("WRITE_REVIEW", "VIEW_DETAILS");
                break;
            case "CONFIRMED":
                // Nếu không có giờ thì dùng 00:00 cho an toàn
                LocalTime effectiveStart = startTime != null ? startTime : LocalTime.MIDNIGHT;
                // Ghép ngày + giờ thành một mốc thời gian cụ thể
                LocalDateTime appointmentDateTime =
                        LocalDateTime.of(booking.getAppointmentDate(), effectiveStart);
                // Tính số phút từ bây giờ đến lịch hẹn
                long minutesUntil = ChronoUnit.MINUTES.between(LocalDateTime.now(ZONE), appointmentDateTime);
                // Còn đủ thời gian → cho hủy, không đủ → chỉ xem
                if (minutesUntil >= CANCEL_THRESHOLD_MINUTES) {
                    actions = List.of("CANCEL", "VIEW_DETAILS");
                } else {
                    actions = List.of("VIEW_DETAILS");
                }
                break;
            // CHECK_IN, WASHING, và mọi status khác → chỉ xem
            default:
                actions = List.of("VIEW_DETAILS");
        }
        return actions;
    }

    /**
     *
     * Chức năng: Tạo mới một booking và xử lý toàn bộ nghiệp vụ liên quan như
     * kiểm tra vehicle, service package, addon service, slot booking, tính giá
     * và áp dụng voucher.
     * <p>
     * Quy trình:
     * - Lấy thông tin vehicle và kiểm tra vehicle tồn tại.
     * - Lấy thông tin service package được chọn.
     * - Lấy danh sách addon service từ request.
     * - Kiểm tra slot booking có tồn tại đầy đủ hay không.
     * - Validate slot theo điều kiện:
     * + Slot thuộc cùng station.
     * + Slot liên tục theo thời gian.
     * + Slot còn đủ capacity.
     * - Tính tổng giá service package và addon service.
     * - Kiểm tra voucher nếu được cung cấp.
     * - Tính số tiền giảm giá và tổng tiền cuối cùng.
     * - Mapping dữ liệu sang Booking entity.
     * - Lưu booking vào database.
     * - Trả về thông tin booking sau khi tạo thành công.
     *
     * @param request thông tin cần thiết để tạo booking bao gồm vehicle,
     *                service package, addon, slot và voucher
     * @return CreateBookingResponse chứa thông tin booking được tạo
     * @author Phong
     * @version 1.0
     */
    @Override
    @Transactional
    public CreateBookingResponse createBooking(CreateBookingRequest request) {

//        validate đầu vào
        bookingValidator.validateCreateBooking(request);

//        lấy customer
        Long userId = getCurrentUserId();
        CustomerContract customer = customerPort.getCustomerByUserId(userId);

//        lấy vehicle
        VehicleContract vehicle = vehiclePort.getById(request.getVehicleId());

//        lấy service package
        ServicePackageContract servicePackage = servicePackagePort.getById(request.getServicePackageId());

        // PRICE CALCULATION
//        tính giá service package
        BigDecimal packagePrice = getServicePackagePrice(request.getVehicleId(), request.getServicePackageId(), LocalDate.parse(request.getAppointmentDate()));

//        tính giá addon service
        BigDecimal addonPrice = getAddonServicePrice(request.getAddonServiceIds());


//        tổng tiền của service package và addon package
        BigDecimal subTotal = packagePrice.add(addonPrice);

        // VOUCHER
        BigDecimal discount = BigDecimal.ZERO;

        VoucherContract voucher = null;
        if (request.getVoucherCode() != null) {

            voucher = voucherPort.getDiscountPercent(request.getVoucherCode(), subTotal.intValue());
            if (!voucher.isValid()) {
                throw new BusinessException(ErrorCode.VOUCHER_INVALID);
            }

            discount = subTotal.multiply(
                    BigDecimal.valueOf(voucher.getDiscountPercentage())
                            .divide(BigDecimal.valueOf(100))
            );

            subTotal = subTotal.subtract(discount);
        }

        BookingType bookingType = getBookingType(vehicle.getId(), servicePackage.getId());

        // BUILD BOOKING ENTITY (FIXED)
        Booking booking = Booking.builder()
                .customer(modelMapper.map(customer, Customer.class))
                .vehicle(modelMapper.map(vehicle, Vehicle.class))
                .servicePackage(modelMapper.map(servicePackage, ServicePackage.class))
                .appointmentDate(LocalDate.parse(request.getAppointmentDate()))
                //chờ khách chuyển khoản cọc — webhook SePay sẽ chuyển sang CONFIRMED
                .status(BookingStatus.CONFIRMED.toString())
                .bookingType(bookingType.toString())
                .totalServiceAmount(packagePrice)
                .totalAddonAmount(addonPrice)
                .voucherDiscountAmount(discount)
                .totalAmount(subTotal)
                .build();


//        tạo BookingAddon
        booking = createBookingAddon(booking, request.getAddonServiceIds());


        // SLOT ALLOCATION
//        tạo booking slot allocation và cộng biến đếm bookingCount trong booking slot;
        booking = createBookingAlocation(booking, request.getSlotIds());

//        tạo voucherUsage
        createVoucherUsage(voucher, booking);

        Booking saved = bookingRepository.save(booking);

//        tạo booking invoice
        bookingInvoicePort.createInvoice(booking);

        //thông tin chuyển khoản cọc cho FE hiển thị QR/hướng dẫn
        String transferContent = "BK" + saved.getId();
        return CreateBookingResponse.builder()
                .bookingId(saved.getId())
                .status(saved.getStatus())
                .totalAmount(subTotal)
                .slotIds(request.getSlotIds())
                .depositAmount(DEFAULT_DEPOSIT_AMOUNT)
                .transferContent(transferContent)
                .qrImageUrl(paymentQrPort.buildQrImageUrl(DEFAULT_DEPOSIT_AMOUNT, transferContent))
                .build();
    }

    private BookingType getBookingType(Long vehicleId, Integer servicePackageId) {
        if (hasSubscription(vehicleId, servicePackageId)){
            return BookingType.SUBSCRIPTION;
        }
        return BookingType.ADVANCE;
    }

    private void createVoucherUsage(VoucherContract voucherContract, Booking booking) {
        if (voucherContract == null) return;
        voucherUsagePort.consumeVoucher(voucherContract.getId(), booking);
    }

    private Booking createBookingAlocation(Booking booking, List<Long> slotIds) {
        List<BookingSlot> slots = bookingSlotRepository.findAvailableSlots(slotIds);
        List<BookingSlotAllocation> allocations = new ArrayList<>();
        for (BookingSlot slot : slots) {

            int updated = bookingSlotRepository.increaseBookedCount(slot.getId());
            if (updated == 0) {
                throw new BusinessException(ErrorCode.BOOKING_SLOT_NOT_AVAILABLE);
            }
            BookingSlotAllocationId id = BookingSlotAllocationId.builder()
                    .bookingSlotId(slot.getId())
                    .bookingId(booking.getId())
                    .build();
            BookingSlotAllocation allocation = BookingSlotAllocation.builder()
                    .id(id)
                    .booking(booking)
                    .bookingSlot(slot)
                    .build();
            allocations.add(allocation);
        }
        booking.setSlotAllocations(allocations);

        return booking;
    }


    private Booking createBookingAddon(Booking booking, List<Integer> addonIds) {

        if (addonIds == null || addonIds.isEmpty()) {
            return booking;
        }

        List<AddonServiceContract> addonServiceContracts = addonServicePort.getByIds(addonIds);

        for (AddonServiceContract addonContract : addonServiceContracts) {
            BookingAddon addon = BookingAddon.builder()
                    .booking(booking)
                    .addonService(modelMapper.map(addonContract, AddonService.class))
                    .price(addonContract.getPrice())
                    .build();

            booking.getAddons().add(addon);
        }

        return booking;
    }

    /**
     * Tính giá tiền của service package
     */
    private BigDecimal getServicePackagePrice(
            Long vehicleId,
            Integer servicePackageId,
            LocalDate appointmentDate
    ) {
        ServicePackageContract servicePackage =
                servicePackagePort.getServicePackage(servicePackageId);

        boolean isVehicleBookingOnDateAndHasSubscription =
                isVehicleBookingOnDateAndHasSubscription(vehicleId, servicePackageId, appointmentDate);

        if (isVehicleBookingOnDateAndHasSubscription) {
            return BigDecimal.ZERO;
        }

        return servicePackage.getBasePrice();
    }

    /**
     * Kiểm tra xe có subscription và đã có booking trong ngày hay chưa
     */
    private boolean isVehicleBookingOnDateAndHasSubscription(
            Long vehicleId,
            Integer servicePackageId,
            LocalDate appointmentDate
    ) {

        boolean hasSubscription =
                hasSubscription(vehicleId, servicePackageId);


        boolean isVehicleBookedOnDate =
                isVehicleBookedOnDate(vehicleId, appointmentDate);


        return hasSubscription && !isVehicleBookedOnDate;
    }

    /**
     * Kiểm tra vehicle đã có booking trong ngày
     */
    private boolean isVehicleBookedOnDate(
            Long vehicleId,
            LocalDate appointmentDate
    ) {

        return bookingRepository
                .existsByVehicleIdAndAppointmentDateAndStatusNot(
                        vehicleId,
                        appointmentDate,
                        BookingStatus.CANCELED.toString()
                );
    }

    private boolean hasSubscription(
            Long vehicleId,
            Integer servicePackageId
    ) {

        boolean hasFamily =
                familySubscriptionPort
                        .hasFamilySubscription(vehicleId, servicePackageId);

        boolean hasUnlimited =
                unlimitSubscriptionPort
                        .hasUnlimitSubscription(vehicleId, servicePackageId);

        return hasFamily || hasUnlimited;
    }

    /**
     * tính giá tiền của addon service
     *
     * @return BigDecimal
     */
    private BigDecimal getAddonServicePrice(List<Integer> addonServiceIds) {
        BigDecimal addonPrice;
        boolean haveAddons = addonServiceIds != null && !addonServiceIds.isEmpty();

        if (haveAddons) {
            addonPrice = addonServicePort.calculateAddonPrice(addonServiceIds);
        } else {
            addonPrice = BigDecimal.ZERO;
        }

        return addonPrice;
    }

    /**
     *
     * Chức năng: Lấy customerId hiện tại đang thực hiện thao tác booking.
     * <p>
     * Quy trình:
     * - Lấy thông tin user hiện tại từ JWT hoặc session.
     * - Mapping user sang customerId tương ứng.
     * - Trả về customerId phục vụ các nghiệp vụ booking.
     *
     * @return id của customer hiện tại
     */
    private Long getCurrentUserId() {
        return securityUtils.getCurrentUserId();
//        return 1L;
    }
}