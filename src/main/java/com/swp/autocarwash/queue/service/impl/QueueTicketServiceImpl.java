package com.swp.autocarwash.queue.service.impl;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.loyalty.entity.CustomerTier;
import com.swp.autocarwash.loyalty.repository.CustomerTierRepository;
import com.swp.autocarwash.queue.repository.custom.QueueTicketRepository;
import com.swp.autocarwash.queue.service.QueueTicketService;
import com.swp.autocarwash.system.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class QueueTicketServiceImpl implements QueueTicketService {

    private static final String DEFAULT_TIER_NAME = "MEMBER";

    private final QueueTicketRepository queueTicketRepository;
    private final CustomerTierRepository customerTierRepository;
    private final SystemSettingService systemSettingService;
    /**
     * Hàm sinh số vé hàng đợi đồng bộ theo từng chi nhánh và loại khách hàng (AC05)
     */
    public synchronized String generateTicketNumber(int stationId, boolean isBooked) {
        // Xác định chữ cái đầu tiên: B (Booking trước), W (Walk-in vãng lai)
        String prefix = isBooked ? "B" : "W";

        // Tính khoảng thời gian bắt đầu và kết thúc ngày hôm nay theo múi giờ VN
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalDateTime localStart = today.atStartOfDay();
        Instant startOfDay = localStart.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
        Instant endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);

        // Đếm số vé đã phát trong ngày tại chi nhánh này dưới DB để tăng số thứ tự
        long countToday = queueTicketRepository.countByStationIdAndIssuedAtBetween(
                stationId, startOfDay, endOfDay);

        long nextNumber = countToday + 1;

        // Trả về định dạng chuỗi: Ví dụ W001, B002
        return prefix + String.format("%03d", nextNumber);
    }

    // Tính logic sắp xếp
    @Override
    public Integer computePriorityScore(Customer customer, boolean isBooking) {
        //1. Lấy rank
        CustomerTier tier = (customer != null && customer.getCustomerTier() != null)
                ? customer.getCustomerTier()
                : customerTierRepository.findByTierName(DEFAULT_TIER_NAME)
                        .orElseThrow(() -> new BusinessException(ErrorCode.TIER_NOT_FOUND));
        //2. Lấy hệ số của rank ( MEMBER=0, SILVER=1, GOLD=2, PLATINUM=3 )
        int tierWeight = tier.getQueuePriorityWeight() != null ? tier.getQueuePriorityWeight() : 0; // 0 là dành cho mấy đứa vãng lai, không có rank thì mặc định nó là 0
        //3. điểm ưu tiên nếu khách đã đặt lịch trước (booking). Khách vãng lai chưa có tài khoản hoặc không booking trước  → mặc định = 0
        int bookingWeight = isBooking ? systemSettingService.getQueuePriorityBookingWeight() : 0;

        return tierWeight + bookingWeight;
    }
}
