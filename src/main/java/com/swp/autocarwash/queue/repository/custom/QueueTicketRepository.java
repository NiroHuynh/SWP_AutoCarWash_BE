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
            "ORDER BY COALESCE(c.customerTier.queuePriorityWeight, 0) DESC, q.isBooking DESC, q.issuedAt ASC")
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
            "WHERE q.station.id = :stationId AND b.status IN :statuses " +
            "ORDER BY COALESCE(c.customerTier.queuePriorityWeight, 0) DESC, q.isBooking DESC, q.issuedAt ASC")
    // Hạng là tiêu chí chính (không thể bị điểm booking đè qua mặt), có đặt lịch trước
    // chỉ tiebreak giữa các khách CÙNG hạng, cùng hạng+cùng loại thì FIFO theo issuedAt.
    //COALESCE(x, 0): nếu x là NULL (khách không có tier — ví dụ walk-in không có tài khoản,
    // customer null nên customerTier cũng null) thì thay bằng 0
    // q.isBooking DESC: Chỉ được xét đến khi 2 dòng có cùng giá trị khóa 1 (cùng hạng, hoặc cùng "không có hạng" = 0),
    // isBooking là boolean (true/false), true > false khi sort
    //q.issuedAt ASC: Chỉ được xét đến khi cả khóa 1 và khóa 2 đều bằng nhau (cùng hạng, cùng loại booking/walk-in)
    //nguyên tắc FIFO "ai đến trước xử lý trước"


    List<QueueTicket> findActiveQueueByStation(
            @Param("stationId") Integer stationId,
            @Param("statuses") List<String> statuses
    );
}
