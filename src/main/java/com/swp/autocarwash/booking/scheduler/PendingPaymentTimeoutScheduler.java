package com.swp.autocarwash.booking.scheduler;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.BookingSlot;
import com.swp.autocarwash.booking.entity.BookingSlotAllocation;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.port.SystemSettingPort;
import com.swp.autocarwash.booking.port.VoucherUsagePort;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.repository.BookingSlotAllocationRepository;
import com.swp.autocarwash.booking.repository.BookingSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Chức năng: Job tự động hủy booking online đang PENDING quá hạn chuyển khoản
 * cọc (mặc định 15 phút — setting PENDING_PAYMENT_TIMEOUT_MINUTES). Khi hủy:
 * trả lại slot (giảm bookedCount) và trả lại voucher đã consume, để khách khác
 * đặt được. Khách chuyển khoản trễ sau khi hủy sẽ được webhook SePay log
 * INVALID_BOOKING_STATE cho admin đối soát/hoàn tiền thủ công.
 *
 * @author Ngân
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PendingPaymentTimeoutScheduler {

    private final BookingRepository bookingRepository;
    private final BookingSlotAllocationRepository bookingSlotAllocationRepository;
    private final BookingSlotRepository bookingSlotRepository;
    private final SystemSettingPort systemSettingPort;
    private final VoucherUsagePort voucherUsagePort;

    /**
     * Chạy mỗi phút: tìm booking PENDING tạo quá hạn timeout, chuyển CANCELED,
     * nhả slot và trả voucher (mirror logic hủy trong BookingServiceImpl.cancelBooking,
     * giữ lại allocation row như hiện trạng).
     */
    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void cancelExpiredPendingBookings() {
        try {
            int timeoutMinutes = systemSettingPort.getPendingPaymentTimeoutMinutes();
            LocalDateTime cutoff = LocalDateTime.now().minusMinutes(timeoutMinutes);

            List<Booking> expiredBookings = bookingRepository
                    .findByStatusAndCreatedAtBefore(BookingStatus.PENDING.name(), cutoff);

            int canceled = 0;
            for (Booking booking : expiredBookings) {
                // webhook có thể vừa confirm xong trong cùng phút — chỉ hủy khi chưa đóng cọc
                if (Boolean.TRUE.equals(booking.getIsDepositPaid())) {
                    continue;
                }

                booking.setStatus(BookingStatus.CANCELED.name());
                booking.setCanceledAt(LocalDateTime.now());
                bookingRepository.save(booking);

                releaseSlots(booking.getId());
                voucherUsagePort.releaseVoucher(booking.getId());
                canceled++;

                log.info("Hủy booking {} vì quá {} phút chưa chuyển khoản cọc",
                        booking.getId(), timeoutMinutes);
            }

            if (canceled > 0) {
                log.info("PendingPaymentTimeoutScheduler: đã hủy {} booking quá hạn cọc", canceled);
            }
        } catch (Exception e) {
            log.error("PendingPaymentTimeoutScheduler lỗi: {}", e.getMessage(), e);
        }
    }

    private void releaseSlots(Long bookingId) {
        List<BookingSlotAllocation> allocations =
                bookingSlotAllocationRepository.findByBookingId(bookingId);
        for (BookingSlotAllocation allocation : allocations) {
            BookingSlot slot = allocation.getBookingSlot();
            if (slot.getBookedCount() != null && slot.getBookedCount() > 0) {
                slot.setBookedCount(slot.getBookedCount() - 1);
                bookingSlotRepository.save(slot);
            }
        }
    }
}
