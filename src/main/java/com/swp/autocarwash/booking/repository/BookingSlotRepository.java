package com.swp.autocarwash.booking.repository;

import com.swp.autocarwash.booking.entity.BookingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * Chức năng: BookingSlotRepository cung cấp các phương thức truy cập dữ liệu
 * booking slot trong database. Repository này hỗ trợ truy vấn slot theo station,
 * ngày đặt lịch và danh sách slot được chọn phục vụ quá trình booking.
 *
 * @author Phong
 * @version 1.0
 */
@Repository
public interface BookingSlotRepository extends JpaRepository<BookingSlot, Long> {

    /**
     *
     * Chức năng: Lấy danh sách booking slot của một station trong một ngày cụ thể
     * và sắp xếp theo thời gian bắt đầu tăng dần (ngoài ra chỉ hiển thị slot từ thời điểm hiện tại).
     *
     */
    @Query("""
                SELECT s
                FROM BookingSlot s
                WHERE s.station.id = :stationId
                AND s.date = :date
                AND (
                    s.date > :today
                    OR s.startTime >= :currentTime
                )
                ORDER BY s.startTime ASC
            """)
    List<BookingSlot> findAvailableSlotsByStationAndDate(
            @Param("stationId") Integer stationId,
            @Param("date") LocalDate date,
            @Param("today") LocalDate today,
            @Param("currentTime") LocalTime currentTime
    );

    /**
     *
     * Chức năng: Lấy danh sách booking slot dựa trên danh sách id được cung cấp.
     * <p>
     * Quy trình:
     * - Nhận danh sách slot id cần tìm.
     * - Truy vấn các slot có id nằm trong danh sách.
     * - Trả về danh sách booking slot tương ứng.
     *
     * @param ids danh sách id của các slot cần lấy
     * @return danh sách BookingSlot tương ứng với các id truyền vào
     * @author Phong
     * @version 1.0
     */
    List<BookingSlot> findByIdIn(List<Long> ids);


    @Query("""
                SELECT s
                FROM BookingSlot s
                WHERE s.id IN :ids
                AND s.status = 'AVAILABLE'
                AND s.bookedCount < s.maxCapacity
            """)
    List<BookingSlot> findAvailableSlots(
            @Param("ids") List<Long> ids
    );


    @Modifying
    @Query("""
                UPDATE BookingSlot s
                SET s.bookedCount = s.bookedCount + 1
                WHERE s.id = :id
                AND s.bookedCount < s.maxCapacity
            """)
    int increaseBookedCount(
            Long id
    );


//    kiểm tra xem slot đã hết hạn chưa
    @Query("""
                SELECT CASE WHEN COUNT(bs.id) > 0 THEN true ELSE false END
                FROM BookingSlot bs
                WHERE bs.id IN :slotIds
                  AND (
                      bs.date < :currentDate
                      OR (
                          bs.date = :currentDate
                          AND bs.startTime < :currentTime
                      )
                  )
            """)
    boolean existsExpiredSlot(
            @Param("slotIds") List<Long> slotIds,
            @Param("currentDate") LocalDate currentDate,
            @Param("currentTime") LocalTime currentTime
    );
}
