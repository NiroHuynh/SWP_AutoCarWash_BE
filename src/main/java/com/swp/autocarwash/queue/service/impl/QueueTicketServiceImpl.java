package com.swp.autocarwash.queue.service.impl;

import com.swp.autocarwash.queue.repository.custom.QueueTicketRepository;
import com.swp.autocarwash.queue.service.QueueTicketService;
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

    private final QueueTicketRepository queueTicketRepository;
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
}
