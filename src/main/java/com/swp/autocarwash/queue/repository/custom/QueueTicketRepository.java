package com.swp.autocarwash.queue.repository.custom;

import com.swp.autocarwash.queue.entity.QueueTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface QueueTicketRepository extends JpaRepository<QueueTicket, Long> {
    //đếm số vé đã cấp cho 1 station trong ngày hiện tại - dùng để sinh ticket_number tăng dần
    //appointment-independent: dựa theo issued_at trong ngày.
    long countByStationIdAndIssuedAtBetween(
            Integer stationId, LocalDateTime startOfDay, LocalDateTime endOfDay);


}
