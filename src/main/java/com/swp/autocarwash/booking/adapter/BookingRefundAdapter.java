package com.swp.autocarwash.booking.adapter;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.BookingSlot;
import com.swp.autocarwash.booking.entity.BookingSlotAllocation;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.event.BookingCanceledEvent;
import com.swp.autocarwash.booking.event.BookingEventPublisher;
import com.swp.autocarwash.booking.port.SystemSettingPort;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.repository.BookingSlotAllocationRepository;
import com.swp.autocarwash.booking.repository.BookingSlotRepository;
import com.swp.autocarwash.common.contract.booking.RefundableBookingContract;
import com.swp.autocarwash.common.exception.ForbiddenException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.refund.port.BookingRefundPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

import static com.swp.autocarwash.system.service.impl.SystemSettingServiceImpl.DEFAULT_DEPOSIT_AMOUNT;

/**
 * Chức năng: Adapter phía module booking cung cấp {@link BookingRefundPort} cho module refund.
 * Đọc snapshot booking và chuyển booking sang REFUND_PENDING khi khách hủy để hoàn tiền.
 *
 * @author KimNgan
 * @version 1.0
 */
@Component
@Profile("pro")
@RequiredArgsConstructor
public class BookingRefundAdapter implements BookingRefundPort {

    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final BookingRepository bookingRepository;
    private final BookingSlotAllocationRepository bookingSlotAllocationRepository;
    private final BookingSlotRepository slotRepository;
    private final BookingEventPublisher bookingEventPublisher;
    private final SystemSettingPort systemSettingPort;

    @Override
    @Transactional(readOnly = true)
    public RefundableBookingContract loadRefundableBooking(Long bookingId, Long customerId) {
        Booking booking = bookingRepository.findDetailById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND));

        Customer customer = booking.getCustomer();
        if (customer == null || !customer.getId().equals(customerId)) {
            throw new ForbiddenException(ErrorCode.REFUND_ACCESS_DENIED);
        }

        LocalTime earliestStart = bookingSlotAllocationRepository.findByBookingId(bookingId).stream()
                .map(BookingSlotAllocation::getBookingSlot)
                .map(BookingSlot::getStartTime)
                .filter(java.util.Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);

        return RefundableBookingContract.builder()
                .bookingId(booking.getId())
                .status(booking.getStatus())
                .isDepositPaid(booking.getIsDepositPaid())
                .appointmentDate(booking.getAppointmentDate())
                .slotStartTime(earliestStart)
                .depositAmount(systemSettingPort.getDepositAmount(DEFAULT_DEPOSIT_AMOUNT))
                .build();
    }

    @Override
    @Transactional
    public void markRefundPending(Long bookingId) {
        Booking booking = bookingRepository.findDetailById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND));

        booking.setStatus(BookingStatus.REFUND_PENDING.name());
        booking.setCanceledAt(LocalDateTime.now(ZONE));
        bookingRepository.save(booking);

        // Giải phóng slot: giảm bookedCount, giữ nguyên allocation để vẫn hiển thị được giờ đã đặt.
        List<BookingSlotAllocation> allocations =
                bookingSlotAllocationRepository.findByBookingId(bookingId);
        for (BookingSlotAllocation allocation : allocations) {
            BookingSlot slot = allocation.getBookingSlot();
            slot.setBookedCount(slot.getBookedCount() - 1);
            slotRepository.save(slot);
        }

        // Chỉ bắn event khi xe đã check-in (checkInEmployee != null) — mirror cancelBooking().
        // Booking đủ điều kiện hoàn tiền là CONFIRMED (chưa check-in) nên thường không phát event.
        if (booking.getCheckInEmployee() != null) {
            bookingEventPublisher.publishBookingCanceled(BookingCanceledEvent.builder()
                    .customerId(booking.getCustomer() != null ? booking.getCustomer().getId() : null)
                    .vehicleId(booking.getVehicle().getId())
                    .bookingId(bookingId)
                    .canceledByStaffId(null)
                    .bookingType(booking.getBookingType())
                    .isDepositPaid(booking.getIsDepositPaid())
                    .checkInAt(booking.getCheckInAt().atZone(ZONE).toInstant())
                    .canceledAt(booking.getCanceledAt().atZone(ZONE).toInstant())
                    .build());
        }
    }

    @Override
    @Transactional
    public void markRefunded(Long bookingId) {
        Booking booking = bookingRepository.findDetailById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND));

        booking.setStatus(BookingStatus.REFUNDED.name());
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void cancelWithoutRefund(Long bookingId) {
        Booking booking = bookingRepository.findDetailById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND));

        booking.setStatus(BookingStatus.CANCELED.name());
        booking.setCanceledAt(LocalDateTime.now(ZONE));
        bookingRepository.save(booking);

        List<BookingSlotAllocation> allocations =
                bookingSlotAllocationRepository.findByBookingId(bookingId);
        for (BookingSlotAllocation allocation : allocations) {
            BookingSlot slot = allocation.getBookingSlot();
            slot.setBookedCount(slot.getBookedCount() - 1);
            slotRepository.save(slot);
        }

        if (booking.getCheckInEmployee() != null) {
            bookingEventPublisher.publishBookingCanceled(BookingCanceledEvent.builder()
                    .customerId(booking.getCustomer() != null ? booking.getCustomer().getId() : null)
                    .vehicleId(booking.getVehicle().getId())
                    .bookingId(bookingId)
                    .canceledByStaffId(null)
                    .bookingType(booking.getBookingType())
                    .isDepositPaid(booking.getIsDepositPaid())
                    .checkInAt(booking.getCheckInAt().atZone(ZONE).toInstant())
                    .canceledAt(booking.getCanceledAt().atZone(ZONE).toInstant())
                    .build());
        }
    }
}
