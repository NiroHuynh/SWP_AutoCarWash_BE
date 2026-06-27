package com.swp.autocarwash.queue.repository.custom;


import com.swp.autocarwash.queue.entity.QueueTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Chức năng: QueueTicketRepository cung cấp truy vấn dữ liệu cho QueueTicket entity.
 *
 * <p>Dùng JOIN FETCH để tải sẵn booking → vehicle/customer và station,
 * tránh N+1 query khi map sang {@code QueueTicketResponse} cho dashboard.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
public interface QueueTicketRepository extends JpaRepository<QueueTicket,Long> {
    /**
     * Tìm queue ticket qua booking id tương ứng để khi cập nhập trạng thái trên booking thì trạng thái trên
     * queue ticket cũng được cập nhập ( đồng bộ, nhất quán
     * @param id của booking id để tìm queue ticket tương ứng
     * @return Optional chứa queue ticket nếu tồn tại, return null nếu không tồn tại
     */
    public Optional<QueueTicket> findQueueTicketByBookingId(Long id);

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
            "WHERE q.station.id = :stationId AND q.status IN :statuses " +
            "ORDER BY q.priorityScore DESC, q.issuedAt ASC")
    List<QueueTicket> findActiveQueueByStation(
            @Param("stationId") Integer stationId,
            @Param("statuses") List<String> statuses
    );
}
