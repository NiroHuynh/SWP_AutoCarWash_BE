package com.swp.autocarwash.booking.scheduler;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.port.SystemSettingPort;
import com.swp.autocarwash.booking.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j  //thu vien ghi log do lombook cung cap
public class DepositConfiscationScheduler {
    /**
     * Subtask 4.1: Cron Job chạy lúc 23:59 mỗi ngày để tịch thu tiền cọc của các
     * Booking bị NO_SHOW trong ngày, chưa được chuyển sang Order Walk-in nào.
     *
     * LƯU Ý QUAN TRỌNG: số tiền cọc bị tịch thu KHÔNG đọc từ booking.deposit_amount,
     * vì mức cọc trong hệ thống này là 1 con số CỐ ĐỊNH áp dụng chung cho mọi booking
     * (ví dụ luôn luôn 20.000đ), được Admin cấu hình trong bảng system_setting.
     * Scheduler chỉ cần đọc ĐÚNG 1 LẦN giá trị này mỗi khi job chạy, rồi áp dụng
     * cho tất cả các booking tìm được.
     */

    private final BookingRepository bookingRepository;
    private final SystemSettingPort systemSettingPort;

    /**
     * Chạy vào 23:59:00 mỗi ngày (giờ server).
     * Cron format Spring: giây phút giờ ngày-trong-tháng tháng ngày-trong-tuần.
     */
    //test tự động thu cọc cuối ngày thì set 10s để demo
    //test tự động transfer cọc booking trong ngày thì set 23:59
    //@Scheduled(cron = "0 59 23 * * ? ")
    @Scheduled(cron = "*/10 * * * * ?")
    @Transactional  //rollback nếu thất bại giữa chừng
    public void confiscateNoShowDeposit(){
        LocalDate today = LocalDate.now();
        log.info("[DepositConfiscationScheduler] Start job for appointmentDate={}", today);

        List<Booking> pendingBookings = bookingRepository.findNoShowBookingsPendingDepositConfiscation(today, BookingStatus.NO_SHOW.toString());
        if(pendingBookings.isEmpty()){
            log.info("[DepositConfiscationScheduler] No NO_SHOW booking with pending deposit found for {}",today);
            return;
        }
        // Đọc mức cọc cố định từ system_setting - dùng chung cho TẤT CẢ booking ở dưới,
        // không đọc lại trong vòng for vì giá trị này không đổi giữa các booking.
        BigDecimal fixedDepositAmount = systemSettingPort.getDepositAmount("DEFAULT_DEPOSIT_AMOUNT");

        int processedCount = 0;
        BigDecimal totalConfiscated = BigDecimal.ZERO;

        for (Booking booking: pendingBookings){
            booking.setDepositConfiscatedAt(LocalDateTime.now());
            bookingRepository.save(booking);

            totalConfiscated = totalConfiscated.add(fixedDepositAmount);
            processedCount++;

            log.info("[DepositConfiscationScheduler] Confiscated {} VND from bookingId={}",
                    fixedDepositAmount, booking.getId());
        }
        log.info("[DepositConfiscationScheduler] Done. Processed {} bookings, total confiscated = {} VND",
                processedCount, totalConfiscated);
    }

}
