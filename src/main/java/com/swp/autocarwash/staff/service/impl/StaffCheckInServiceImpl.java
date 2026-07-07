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
import com.swp.autocarwash.payment.entity.BookingInvoice;
import com.swp.autocarwash.payment.repository.BookingInvoiceRepository;
import com.swp.autocarwash.promotion.entity.VoucherUsage;
import com.swp.autocarwash.promotion.repository.VoucherUsageRepository;
import com.swp.autocarwash.queue.entity.QueueTicket;
import com.swp.autocarwash.queue.entity.enums.QueueStatus;
import com.swp.autocarwash.queue.repository.custom.QueueTicketRepository;
import com.swp.autocarwash.staff.dto.response.CheckInResultResponse;
import com.swp.autocarwash.staff.dto.response.ScanVehicleResponse;
import com.swp.autocarwash.system.service.impl.SystemSettingServiceImpl;
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

    private final BookingInvoiceRepository bookingInvoiceRepository;


    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final BookingSlotAllocationRepository bookingSlotAllocationRepository;
    private final WashLaneRepository washLaneRepository;
    private final QueueTicketRepository queueTicketRepository;
    private final SystemSettingServiceImpl systemSettingServiceImpl;
    private final VoucherUsageRepository voucherUsageRepository;

    //check và trả về thông tin booking khi input license

    @Override
    public ScanVehicleResponse scanVehicle(String licensePlate) {
        Optional<Booking> bookingOtp = bookingRepository.findConfirmedBookingTodayByLicensePlate(licensePlate);

        // TRƯỜNG HỢP 1: KHÔNG TÌM THẤY LỊCH ĐẶT TRƯỚC (KHÁCH VÃNG LAI)
        if (bookingOtp.isEmpty()) {
            Vehicle vehicle = vehicleRepository.findByLicensePlateAndIsDeletedFalse(licensePlate).orElse(null);

            // Trả về tối giản các trường định danh cơ bản, để FE biết đường mở Form Walk-in
            return ScanVehicleResponse.builder()
                    .licensePlate(licensePlate)
                    .hasBooking(false)
                    .isVehiclePenalized(vehicle != null && isVehiclePenalized(vehicle))
                    .brandName(vehicle != null ? vehicle.getBrandName() : null)
                    .color(vehicle != null ? vehicle.getColor() : null)
                    .build();
        }

        // TRƯỜNG HỢP 2: TÌM THẤY LỊCH ĐẶT TRƯỚC HỢP LỆ
        Booking booking = bookingOtp.get();
        Vehicle vehicle = booking.getVehicle();

        // Lấy thông tin khách hàng & phân hạng thành viên
        Customer customer = booking.getCustomer();
        String customerName = customer != null ? customer.getFullName() : "Customer no have name(WALK_IN customer)";
        String customerTier = (customer != null && customer.getCustomerTier().getTierName() != null) ? customer.getCustomerTier().getTierName() : "MEMBER";

        // Lấy thông tin khung giờ (Slot) phục vụ
        List<BookingSlot> slots = bookingSlotAllocationRepository.findBookingSLotsByBookingId(booking.getId());
        LocalTime startTime = slots.isEmpty() ? null : slots.get(0).getStartTime();
        LocalTime endTime = slots.isEmpty() ? null : slots.get(slots.size() - 1).getEndTime();

        //  TRUY VẾT STATION THÔNG QUA BOOKING SLOT
        String stationName = "None";
        String stationAddress = "None";

        if (!slots.isEmpty()) {
            // Lấy slot đầu tiên được phân bổ cho đơn đặt lịch này
            BookingSlot firstSlot = slots.getFirst();
            if (firstSlot.getStation() != null) {
                stationName = firstSlot.getStation().getStationName();
                stationAddress = firstSlot.getStation().getAddress();
            }
        }



        // 4. Lấy thông tin hóa đơn tài chính chi tiết từ BookingInvoice
        Optional<BookingInvoice> invoiceOpt = bookingInvoiceRepository.findByBooking_Id(booking.getId());

        BigDecimal totalAmount = booking.getTotalAmount() != null ? booking.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal remainingAmount = totalAmount; // Mặc định thu bằng tổng tiền nếu không tìm thấy invoice
        BigDecimal servicePrice = booking.getTotalServiceAmount() != null ? booking.getTotalServiceAmount() : BigDecimal.ZERO;
        BigDecimal voucherDiscountAmount = booking.getVoucherDiscountAmount() != null ? booking.getVoucherDiscountAmount() : BigDecimal.ZERO;
        String serviceName = booking.getServicePackage() != null
                ? booking.getServicePackage().getName()
                : null;

        if (invoiceOpt.isPresent()) {
            BookingInvoice invoice = invoiceOpt.get();
            totalAmount = invoice.getRawAmount();
            remainingAmount = invoice.getFinalAmount(); // Số tiền thực tế cần thu nốt tại quầy (Checkout)
            servicePrice = invoice.getServiceAmount();
            voucherDiscountAmount = invoice.getVoucherDiscount() != null ? invoice.getVoucherDiscount() : BigDecimal.ZERO;
        }

        // TRUY VẾT VOUCHER CODE QUA MỐI QUAN HỆ BẢNG VOUCHER_USAGE
        Optional<VoucherUsage> voucherUsageOpt = voucherUsageRepository.findByBooking_Id(booking.getId());
        String voucherCode = null;
        Integer voucherDiscountPercent = null;

        if (voucherUsageOpt.isPresent()) {
            VoucherUsage voucherUsage = voucherUsageOpt.get();
            if (voucherUsage.getVoucher() != null) {
                voucherCode = voucherUsage.getVoucher().getVoucherCode(); // Bốc mã code thật
                voucherDiscountPercent = voucherUsage.getVoucher().getDiscountPercentage(); // Bốc % giảm thật
            }
        }

        //Đóng dữ liệu
        return ScanVehicleResponse.builder()
                .bookingId(booking.getId())
                .licensePlate(licensePlate)
                .customerName(customerName)
                .slotStartTime(startTime)
                .slotEndTime(endTime)
                .hasBooking(true)
                .isVehiclePenalized(isVehiclePenalized(vehicle))

                // Điền các trường đồng bộ dữ liệu
                .appointmentDate(booking.getAppointmentDate())
                .bookingType(booking.getBookingType())
                .brandName(vehicle.getBrandName())
                .color(vehicle.getColor())
                .customerTier(customerTier)

                .depositAmount(systemSettingServiceImpl.getDepositAmount(SystemSettingServiceImpl.DEFAULT_DEPOSIT_AMOUNT))
                .depositPaid(booking.getIsDepositPaid() != null && booking.getIsDepositPaid())

                .remainingAmount(remainingAmount)
                .serviceName(serviceName)
                .servicePrice(servicePrice)

                // Thông tin chi nhánh trỏ từ thực thể Slot hoặc Booking
                .stationName(stationName)
                .stationAddress(stationAddress)

                .status(booking.getStatus())
                .technicianName(booking.getCheckInEmployee() != null ? booking.getCheckInEmployee().getFullName() : "NONE")
                .totalAmount(totalAmount)
                .voucherCode(voucherCode)
                .voucherDiscountAmount(voucherDiscountAmount)
                .voucherDiscountPercent(voucherDiscountPercent)
                .build();
    }

    // Hàm phụ check vi phạm của xe giữ nguyên
    private boolean isVehiclePenalized(Vehicle vehicle) {
        return vehicle.getViolationCount() != null && vehicle.getViolationCount() > VIOLATION_LIMIT
                && vehicle.getRestrictedUntil() != null && vehicle.getRestrictedUntil().isAfter(Instant.now());
    }

    //tính độ lệch thời gian check in so với booking(sớm, trễ)
    @Override
    public CheckInResultResponse confirmCheckIn(Long bookingId) {
        Booking booking  = bookingRepository.findById(bookingId).orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));
        List<BookingSlot> slots = bookingSlotAllocationRepository.findBookingSLotsByBookingId(bookingId);
        if (slots.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_ALLOCATED_TIME_SLOT);
        }

        // Lấy thông tin chi nhánh trực tiếp từ ô giờ đã đặt lịch
        BookingSlot firstSlot = slots.get(0);
        if (firstSlot.getStation() == null) {
            throw new BusinessException(ErrorCode.STATION_NOT_FOUND);
        }
        Integer currentStationId = firstSlot.getStation().getId();

        //Tính độ lệch thời gian thực tế so với slot đầu tiên.
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

        boolean hasAvailableLane = washLaneRepository.existsByStationIdAndStatusAndIsDeletedFalse(
                currentStationId, WashLaneStatus.AVAILABLE.toString());

        if (isEarly) {
            // Subtask 3.3: luồng "ĐẾN SỚM" (> 15 phút)
            if (hasAvailableLane) {
                return doCheckIn(booking, slots.get(0), (int) minutesDeviation,
                        "Checked in early - lane available");
            }
            throw new BusinessException(ErrorCode. EARLY_ARRIVAL_SLOT_FULL);
        }

        // Subtask 3.4: luồng "ĐẾN TRỄ" (> 10 phút)
        return handleLateArrival(booking, slots.get(0), (int) minutesDeviation, hasAvailableLane);
    }

    /**
     * Dùng chung cho luồng "đúng giờ" và "đến sớm/trễ nhưng có làn trống":
     * cập nhật Booking sang CHECKED_IN và cấp vé queue_ticket mới.
     */
    private CheckInResultResponse doCheckIn(Booking booking, BookingSlot slot, int minutesDeviation, String message) {
        booking.setStatus(BookingStatus.CHECK_IN.toString());
        booking.setCheckInAt(LocalDateTime.now());
         Booking savedBooking = bookingRepository.save(booking);

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
                .checkInAt(savedBooking.getCheckInAt())
                .build();
    }

    /**
     * Subtask 3.4: xử lý nhánh "đến trễ" khi không còn làn trống.
     * Phân nhánh theo loại booking: Khách lẻ (chuyển Walk-in) hoặc Khách dùng Package (phạt vi phạm).
     */
    private CheckInResultResponse handleLateArrival(Booking booking, BookingSlot slot, int minutesDeviation, boolean hasAvailableLane) {
        // Nếu chi nhánh HIỆN TẠI thực sự còn làn trống -> châm chước cho vào
        if (hasAvailableLane) {
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

}
