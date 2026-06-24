package com.swp.autocarwash.staff.service.impl;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.BookingSlot;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.entity.enums.BookingType;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.repository.BookingSlotAllocationRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.queue.entity.QueueTicket;
import com.swp.autocarwash.queue.entity.enums.QueueStatus;
import com.swp.autocarwash.queue.repository.custom.QueueTicketRepository;
import com.swp.autocarwash.staff.dto.response.CheckInResultResponse;
import com.swp.autocarwash.staff.dto.response.ScanVehicleResponse;
import com.swp.autocarwash.wash.entity.enums.WashLaneStatus;
import com.swp.autocarwash.wash.repository.custom.WashLaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffCheckInServiceImpl implements StaffCheckinService{

    //AC-02: Đến trước tối đa 15 phút vẫn coi là "đúng giờ"
    private static final long EARLY_THRESHOLD_MINUTES = 15;

    // AC-02: Đến sau tối đa 10 phút vẫn coi là "đúng giờ"
    private static final long LATE_THRESHOLD_MINUTES = 10;

    //Số lần vi phạm tối đa được châm chước trước khi bị khóa đặt lịch
    private static final int VIOLATION_LIMIT = 3;

    //Số ngày bị khoá đặt lịch khi vượt quá số lần vi phạm cho phép
    private static final long RESTRICTION_DAYS = 14;



    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final BookingSlotAllocationRepository bookingSlotAllocationRepository;
    private final WashLaneRepository washLaneRepository;
    private final QueueTicketRepository queueTicketRepository;

    //check và trả về thông tin booking khi input license
    @Override
    public ScanVehicleResponse scanVehicle(String licensePlate) {
        Optional<Booking> bookingOtp = bookingRepository.findConfirmedBookingTodayByLicensePlate(licensePlate);
        if(bookingOtp.isEmpty()){
            //không có lịch, booking trước -> FE tự mở màn hình CREATE FOR WALK_IN
            Vehicle vehicle = vehicleRepository.findByLicensePlateAndIsDeletedFalse(licensePlate).orElse(null);
            return ScanVehicleResponse.builder()
                    .licensePlate(licensePlate)
                    .hasBooking(false)
                    .isVehiclePenalized(vehicle != null && isVehiclePenalized(vehicle))
                    .build();
        }

        Booking booking = bookingOtp.get();
        Vehicle vehicle = booking.getVehicle();

        Customer customer = customerRepository.findById(booking.getCustomer().getId()).orElse(null);
        String customerName = customer != null ? customer.getFullName() : null;
        List<BookingSlot> slots = bookingSlotAllocationRepository.findBookingSLotsByBookingId(booking.getId());
        //tính ra khung giờ đặt, khung giờ kết thúc
        LocalTime slotStartTime = slots.isEmpty() ? null : slots.get(0).getStartTime();
        LocalTime slotEndTime = slots.isEmpty() ? null : slots.get(slots.size() - 1).getEndTime();

        return ScanVehicleResponse.builder()
                .bookingId(booking.getId())
                .licensePlate(licensePlate)
                .customerName(customerName)
                .slotStartTime(slotStartTime)
                .slotEndTime(slotEndTime)
                .hasBooking(true)
                .isVehiclePenalized(isVehiclePenalized(vehicle))
                .build();
    }

    private boolean isVehiclePenalized(Vehicle vehicle){
        return vehicle.getRestrictedUntil() != null && vehicle.getRestrictedUntil().isAfter(Instant.now());
    }

    //AC-02: tính độ lệch thời gian check in so với booking(sớm, trễ)
    @Override
    public CheckInResultResponse confirmCheckIn(Long bookingId) {
        Booking booking  = bookingRepository.findById(bookingId).orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));
        List<BookingSlot> slots = bookingSlotAllocationRepository.findBookingSLotsByBookingId(bookingId);
        if (slots.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_ALLOCATED_TIME_SLOT);
        }

        // AC04: Áp dụng TRƯỚC khi xét đúng giờ/sớm/trễ - khách Walk-in đang bị
        // restricted_until (đã từng vi phạm > 3 lần) phải bị chặn check-in cho đến khi
        // Staff thu 20.000đ cọc tại quầy (gọi API collectWalkInPenaltyDeposit trước).
        if (booking.getBookingType().equals(BookingType.WALK_IN.toString()) ) {
            Vehicle vehicle = booking.getVehicle();
            boolean penalized = isVehiclePenalized(vehicle);
            boolean depositCollected = Boolean.TRUE.equals(booking.getIsDepositPaid());

            if (penalized && !depositCollected) {
                throw new BusinessException(ErrorCode.VEHICLE_CHECKIN_RESTRICTED);
            }
        }

        // Subtask 3.2: Tính độ lệch thời gian thực tế so với slot đầu tiên.
        LocalTime scheduledStart = slots.get(0).getStartTime();
        LocalTime now = LocalTime.now();
        long minutesDeviation = Duration.between(scheduledStart, now).toMinutes();
        // > 0: trễ | < 0: sớm | 0: đúng giờ chính xác
        boolean isOnTime = minutesDeviation >= -EARLY_THRESHOLD_MINUTES && minutesDeviation <= LATE_THRESHOLD_MINUTES;
        boolean isEarly = minutesDeviation < -EARLY_THRESHOLD_MINUTES;
        // còn lại: isLate = minutesDeviation > LATE_THRESHOLD_MINUTES

        if (isOnTime) {
            // Subtask 3.3: luồng "ĐÚNG GIỜ"
            return doCheckIn(booking, slots.get(0), (int) minutesDeviation,
                    "Checked in successfully (on time)");
        }

        if (isEarly) {
            // Subtask 3.3: luồng "ĐẾN SỚM" (> 15 phút)
            if (washLaneRepository.existsByStatusAndIsDeletedFalse(WashLaneStatus.AVAILABLE.toString())) {
                return doCheckIn(booking, slots.get(0), (int) minutesDeviation,
                        "Checked in early - lane available");
            }
            throw new BusinessException(ErrorCode. EARLY_ARRIVAL_SLOT_FULL);
        }

        // Subtask 3.4: luồng "ĐẾN TRỄ" (> 10 phút)
        return handleLateArrival(booking, slots.get(0), (int) minutesDeviation);
    }

    /**
     * Dùng chung cho luồng "đúng giờ" và "đến sớm/trễ nhưng có làn trống":
     * cập nhật Booking sang CHECKED_IN và cấp vé queue_ticket mới.
     */
    private CheckInResultResponse doCheckIn(Booking booking, BookingSlot slot, int minutesDeviation, String message) {
        booking.setStatus(BookingStatus.CHECK_IN.toString());
        booking.setCheckInAt(Instant.now());
        bookingRepository.save(booking);

        QueueTicket ticket = QueueTicket.builder()
                .station(slot.getStation())
                .booking(booking)
                .ticketNumber(generateTicketNumber(slot.getStation().getId(),true))
                .status(QueueStatus.WAITING.toString())
                .isBooking(true)
                .build();
        queueTicketRepository.save(ticket);

        return CheckInResultResponse.builder()
                .bookingId(booking.getId())
                .status(BookingStatus.CHECK_IN.toString())
                .queueTicketNumber(ticket.getTicketNumber())
                .minutesDeviation(minutesDeviation)
                .message(message)
                .requiresWalkIn(false)
                .build();
    }

    /**
     * Subtask 3.4: xử lý nhánh "đến trễ" khi không còn làn trống.
     * Phân nhánh theo loại booking: Khách lẻ (chuyển Walk-in) hoặc Khách dùng Package (phạt vi phạm).
     */
    private CheckInResultResponse handleLateArrival(Booking booking, BookingSlot slot, int minutesDeviation) {
        if (washLaneRepository.existsByStatusAndIsDeletedFalse(WashLaneStatus.AVAILABLE.toString())) {
            // Trường hợp 1: Cửa hàng còn làn trống -> châm chước, bỏ qua phạt.
            return doCheckIn(booking, slot, minutesDeviation, "Checked in late - lane available, penalty waived");
        }

        // Trường hợp 2: Cửa hàng bận -> chuyển Booking sang NO_SHOW.
        booking.setStatus(BookingStatus.NO_SHOW.toString());
        bookingRepository.save(booking);

        if (booking.getBookingType().equals(BookingType.SUBSCRIPTION.toString())) {
            //  Khách dùng gói Unlimited/Family -> phạt phải cộng vào
            // CUSTOMER (không phải vehicle), vì gói này không gắn 1 xe cố định.
            Customer customer = customerRepository.findById(booking.getCustomer().getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND));

            int newViolationCount = (customer.getViolationCount() == null ? 0 : customer.getViolationCount()) + 1;
            customer.setViolationCount(newViolationCount);

            if (newViolationCount > VIOLATION_LIMIT) {
                customer.setRestrictedUntil(Instant.now().plus(RESTRICTION_DAYS, ChronoUnit.DAYS));
            }
            customerRepository.save(customer);

            return CheckInResultResponse.builder()
                    .bookingId(booking.getId())
                    .status(BookingStatus.NO_SHOW.toString())
                    .minutesDeviation(minutesDeviation)
                    .message("No-show: violation recorded on customer account (subscription package, count = "
                            + newViolationCount + ")")
                    .requiresWalkIn(false)
                    .build();
        }

        // Nhánh Khách lẻ: yêu cầu Frontend mở màn hình Walk-in, chuyển 100% deposit sang đơn mới.
        return CheckInResultResponse.builder()
                .bookingId(booking.getId())
                .status(BookingStatus.NO_SHOW.toString())
                .minutesDeviation(minutesDeviation)
                .message("No-show: please create a new walk-in order, deposit will be transferred")
                .requiresWalkIn(true)
                .oldBookingId(booking.getId())
                .build();
    }

    public synchronized String generateTicketNumber(int stationId, boolean isBooked) {

        // Bước 2.1: Xác định Prefix theo loại khách
        String prefix = isBooked ? "B" : "W";

        // Bước 2.2: Đếm số vé đã cấp trong ngày hôm nay tại chi nhánh nàystart

        LocalDate today = LocalDate.now();
        LocalDateTime localStart = today.atStartOfDay();
        Instant startOfDay = localStart.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
        Instant endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);

        long countToday = queueTicketRepository.countByStationIdAndIssuedAtBetween(
                stationId, startOfDay, endOfDay);

        long nextNumber = countToday + 1;

        // Bước 2.3: Định dạng chuỗi số đủ 3 chữ số, ghép với Prefix
        return prefix + String.format("%03d", nextNumber);
    }

    // ===================== AC04 - Thu cọc phạt cho Walk-in bị restricted =====================

    @Override
    public CheckInResultResponse collectWalkInPenaltyDeposit(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));

        if (!booking.getBookingType().equals(BookingType.WALK_IN.toString())) {
            throw new BusinessException(ErrorCode.PENALTY_ONLY_FOR_WALK_IN);
        }

        Vehicle vehicle = booking.getVehicle();
        if (!isVehiclePenalized(vehicle)) {
            throw new BusinessException(ErrorCode.VEHICLE_CLEAR_NO_PENALTY);
        }

        booking.setIsDepositPaid(true);
        bookingRepository.save(booking);

        return CheckInResultResponse.builder()
                .bookingId(booking.getId())
                .status(booking.getStatus())
                .message("Collected 20,000 VND penalty deposit. You may now proceed with check-in.")
                .requiresWalkIn(false)
                .build();
    }

}
