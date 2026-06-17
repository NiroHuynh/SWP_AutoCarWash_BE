package com.swp.autocarwash.booking.repository;

import com.swp.autocarwash.booking.entity.Booking;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository truy vấn dữ liệu lịch đặt ({@link Booking}).
 *
 * <p>Sử dụng JOIN FETCH trong JPQL để tải sẵn các quan hệ lazy
 * ({@code vehicle}, {@code servicePackage}), tránh vấn đề N+1 query.
 * Chiều sắp xếp được truyền qua {@link Sort} để tái sử dụng một method
 * cho cả Upcoming (ASC) và Past (DESC).</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Lấy danh sách booking của một khách hàng theo nhóm trạng thái và thứ tự sắp xếp linh hoạt.
     *
     * <p>Dùng chung cho hai tab:
     * <ul>
     *   <li>Upcoming: truyền {@code Sort.by(ASC, "appointmentDate")}</li>
     *   <li>Past: truyền {@code Sort.by(DESC, "appointmentDate")}</li>
     * </ul>
     * Không đặt ORDER BY trong JPQL — Spring Data tự áp dụng {@code sort}.</p>
     *
     * @param customerId mã định danh của khách hàng cần truy vấn
     * @param statuses   danh sách trạng thái hợp lệ
     * @param sort       thứ tự sắp xếp kết quả
     * @return danh sách {@link Booking} thỏa điều kiện, đã eager-fetch quan hệ liên quan
     */
    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN FETCH b.vehicle " +
           "JOIN FETCH b.servicePackage " +
           "WHERE b.customer.id = :customerId AND b.status IN :statuses")
    List<Booking> findByCustomerIdAndStatuses(
            @Param("customerId") Long customerId,
            @Param("statuses") List<String> statuses,
            Sort sort
    );

    /**
     * Lấy thông tin chi tiết một booking theo ID, eager-fetch toàn bộ quan hệ
     * cần thiết để render màn hình chi tiết (AC-25.3.2).
     *
     * @param id mã định danh của lịch đặt
     * @return {@link Optional} chứa {@link Booking} đã eager-fetch,
     *         hoặc rỗng nếu không tìm thấy
     */
    @Query("SELECT b FROM Booking b " +
           "JOIN FETCH b.vehicle " +
           "JOIN FETCH b.servicePackage " +
           "LEFT JOIN FETCH b.checkInEmployee " +
           "WHERE b.id = :id")
    Optional<Booking> findDetailById(@Param("id") Long id);
}
