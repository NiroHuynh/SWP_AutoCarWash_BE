package com.swp.autocarwash.booking.repository;

import com.swp.autocarwash.booking.entity.BookingSlotAllocation;
import com.swp.autocarwash.booking.entity.BookingSlotAllocationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository truy vấn dữ liệu phân bổ slot ({@link BookingSlotAllocation}).
 *
 * <p>Dùng để lấy các khung giờ ({@code BookingSlot}) được gán cho một lịch đặt cụ thể,
 * phục vụ việc hiển thị khoảng thời gian dự kiến trên booking card.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Repository
public interface BookingSlotAllocationRepository
        extends JpaRepository<BookingSlotAllocation, BookingSlotAllocationId> {

    /**
     * Lấy tất cả phân bổ slot của một lịch đặt, sắp xếp theo giờ bắt đầu tăng dần.
     *
     * <p>{@code bookingSlot} được tải cùng lúc (eager) để tránh lazy-loading exception
     * khi truy cập {@code startTime} và {@code endTime} ngoài phạm vi transaction.</p>
     *
     * @param bookingId mã định danh của lịch đặt cần truy vấn
     * @return danh sách {@link BookingSlotAllocation} đã eager-fetch {@code bookingSlot},
     *         sắp xếp theo {@code startTime} tăng dần
     */
    @Query("SELECT bsa FROM BookingSlotAllocation bsa " +
           "JOIN FETCH bsa.bookingSlot bs " +
           "LEFT JOIN FETCH bs.station " +
           "WHERE bsa.booking.id = :bookingId " +
           "ORDER BY bs.startTime ASC")
    List<BookingSlotAllocation> findByBookingId(@Param("bookingId") Long bookingId);
}
