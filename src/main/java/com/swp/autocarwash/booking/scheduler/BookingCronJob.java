package com.swp.autocarwash.booking.scheduler;


import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCronJob {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;

    private static final int VIOLATION_LIMIT = 3;
    private static final long RESTRICTION_DAYS = 14;

    //CẤU HÌNH MỚI: Đúng 23:50:00 hàng đêm, theo múi giờ Việt Nam
    @Scheduled(cron = "0 50 23 * * ?", zone = "Asia/Ho_Chi_Minh")
    @Transactional
    public void autoCleanDeadBookings() {
        log.info("--- CRON-JOB: Bắt đầu dọn dẹp đơn bùng hẹn cuối ngày ---");

        //Chạy lúc 23:50 nên targetDate chính là ngày HÔM NAY luôn
        LocalDate targetDate = LocalDate.now();
        log.info("Ngày cần quét dữ liệu: {}", targetDate);

        try {
            // LUỒNG 1: Xử lý phạt vi phạm cộng điểm cho nhóm dùng gói Membership (Family/Unlimited)
            List<Booking> subBookings = bookingRepository.findUncheckedSubscriptionBookingsToday(targetDate);
            log.info("Tìm thấy {} đơn dùng gói Membership bị bùng hẹn.", subBookings.size());

            for (Booking booking : subBookings) {
                if (booking.getCustomer() != null) {
                    Customer customer = booking.getCustomer();
                    int newViolationCount = (customer.getViolationCount() == null ? 0 : customer.getViolationCount()) + 1;
                    customer.setViolationCount(newViolationCount);

                    // Nếu vượt quá 3 lần vi phạm trong vòng 30 ngày -> Khóa app 14 ngày
                    if (newViolationCount > VIOLATION_LIMIT) {
                        customer.setRestrictedUntil(Instant.now().plus(RESTRICTION_DAYS, ChronoUnit.DAYS));
                        log.info("Khách hàng ID {} bị khóa quyền đặt lịch 14 ngày do vi phạm lần thứ {}", customer.getId(), newViolationCount);
                    }
                    customerRepository.save(customer);
                }
            }

            // LUỒNG 2: Ép trạng thái tất cả các đơn CONFIRMED (bao gồm cả khách rửa lẻ) về NO_SHOW
            int updatedRows = bookingRepository.updateStatusToNoShowForExpiredBookings(targetDate);
            log.info("Cập nhật thành công {} đơn hàng sang trạng thái NO_SHOW.", updatedRows);

        } catch (Exception e) {
            log.error("Lỗi xảy ra trong quá trình chạy Cron-job dọn đơn: ", e);
        }

        log.info("--- CRON-JOB: Hoàn thành dọn dẹp đơn bùng hẹn ---");
    }
}
