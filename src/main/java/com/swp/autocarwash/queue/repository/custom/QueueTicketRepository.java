package com.swp.autocarwash.queue.repository.custom;


import com.swp.autocarwash.queue.entity.QueueTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueueTicketRepository extends JpaRepository<QueueTicket, Long> {
    //đếm số vé đã cấp cho 1 station trong ngày hiện tại - dùng để sinh ticket_number tăng dần
    //appointment-independent: dựa theo issued_at trong ngày.
    long countByStationIdAndIssuedAtBetween(
            Integer stationId, Instant startOfDay, Instant endOfDay);
    /**
     * Chức năng: Trả về danh sách queue ticket có trạng thái WAITING ( CHECK_IN ), danh sách này có booking, vehicle, customer, station tương ứng với queue ticket, được sắp xếp giảm dần theo hạng tier và các sự cố theo thứ tự xảy ra trước, xử lí trước
     * @param statuses
     * @return
     */
    @Query("SELECT DISTINCT q FROM QueueTicket q " +
            "LEFT JOIN FETCH q.booking b " +
            "LEFT JOIN FETCH b.vehicle " +
            "LEFT JOIN FETCH b.servicePackage " +
            "LEFT JOIN FETCH b.customer c " +
            "LEFT JOIN FETCH c.customerTier " +
            "LEFT JOIN FETCH q.station " +
            "WHERE q.status IN :statuses " +
            "ORDER BY q.priorityScore DESC, q.issuedAt ASC")
    public List<QueueTicket> findQueueTicketListByStatus(@Param("statuses") List<String> statuses);

    /**
     * Tìm queue ticket qua booking id tương ứng để khi cập nhập trạng thái trên booking thì trạng thái trên
     * queue ticket cũng được cập nhập ( đồng bộ, nhất quán
     * @param id của booking id để tìm queue ticket tương ứng
     * @return Optional chứa queue ticket nếu tồn tại, return null nếu không tồn tại
     */
    public Optional<QueueTicket> findQueueTicketByBookingId(Long id);
    /**
     * Chức năng: Trả về danh sách queue ticket được coi là "đang active"  của riêng 1 station, dùng để hiện real time trên dashboard của staff tại station đó
     *
     * @param stationId id của station cần lấy danh sách hàng chờ
     * @param statuses  danh sách status được coi là "đang active"
     * @return danh sách QueueTicket thuộc station, đã eager-fetch, sắp theo độ ưu tiên
     */
    @Query("SELECT DISTINCT q FROM QueueTicket q " +
            "LEFT JOIN FETCH q.booking b " +
            "LEFT JOIN FETCH b.vehicle " +
            "LEFT JOIN FETCH b.servicePackage " +
            "LEFT JOIN FETCH b.customer c " +
            "LEFT JOIN FETCH c.customerTier " +
            "LEFT JOIN FETCH q.station " +
            "LEFT JOIN FETCH q.washLane " +
            "WHERE q.station.id = :stationId AND b.status IN :statuses " +
            "ORDER BY q.priorityScore DESC, q.issuedAt ASC")
    List<QueueTicket> findActiveQueueByStation(
            @Param("stationId") Integer stationId,
            @Param("statuses") List<String> statuses
    );
}
