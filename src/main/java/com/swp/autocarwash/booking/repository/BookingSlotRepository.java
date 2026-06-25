package com.swp.autocarwash.booking.repository;

import com.swp.autocarwash.booking.entity.BookingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
public interface BookingSlotRepository extends JpaRepository<BookingSlot, Integer> {

    /**
     *
     * Chức năng: Lấy danh sách booking slot của một station trong một ngày cụ thể
     * và sắp xếp theo thời gian bắt đầu tăng dần.
     *
     * Quy trình:
     * - Nhận stationId và ngày cần tìm kiếm.
     * - Truy vấn các slot thuộc station tương ứng trong ngày đó.
     * - Sắp xếp danh sách slot theo startTime tăng dần.
     * - Trả về danh sách slot tìm được.
     *
     * @param stationId id của station cần lấy danh sách slot
     * @param date ngày cần lấy lịch booking
     *
     * @return danh sách BookingSlot thuộc station trong ngày được chỉ định
     *
     * @author Phong
     * @version 1.0
     */
    List<BookingSlot> findByStationIdAndDateOrderByStartTimeAsc(
            Integer stationId,
            LocalDate date
    );

    /**
     *
     * Chức năng: Lấy danh sách booking slot dựa trên danh sách id được cung cấp.
     *
     * Quy trình:
     * - Nhận danh sách slot id cần tìm.
     * - Truy vấn các slot có id nằm trong danh sách.
     * - Trả về danh sách booking slot tương ứng.
     *
     * @param ids danh sách id của các slot cần lấy
     *
     * @return danh sách BookingSlot tương ứng với các id truyền vào
     *
     * @author Phong
     * @version 1.0
     */
    List<BookingSlot> findByIdIn(List<Integer> ids);

    //Lấy danh sách slot còn trống của chi nhánh trong ngày
    // dùng để hiển thị cho create_walkin chọn slot
    @Query("SELECT bs FROM BookingSlot bs " +
            "WHERE bs.station.id = :stationId " +
            "AND bs.date = :date " +
            "AND bs.status = 'AVAILABLE' " +
            "AND bs.bookedCount < bs.maxCapacity " +
            "ORDER BY bs.startTime ASC")
    List<BookingSlot> findAvailableSlotsByStationAndDate(@Param("stationId") Integer stationId,
                                                         @Param("date") LocalDate date);
}
