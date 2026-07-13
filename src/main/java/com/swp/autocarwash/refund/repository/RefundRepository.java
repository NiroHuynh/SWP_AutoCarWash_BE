package com.swp.autocarwash.refund.repository;

import com.swp.autocarwash.refund.entity.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository truy vấn dữ liệu {@link Refund}.
 *
 * @author KimNgan
 * @version 1.0
 */
@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {

    /** Kiểm tra booking đã có yêu cầu hoàn tiền chưa (đảm bảo 1-1). */
    boolean existsByBookingId(Long bookingId);

    /** Lấy refund gắn với booking (1-1) — dùng để hiển thị thông tin hoàn tiền trên booking card/detail. */
    Optional<Refund> findByBookingId(Long bookingId);

    /**
     * Danh sách hoàn tiền cho Admin (US-05 AC1, AC6-AC8, AC10) — "Chờ xử lý" (status PENDING)
     * lên đầu, trong mỗi nhóm sắp theo created_at tăng dần.
     */
    @Query(value = """
            SELECT DISTINCT r FROM Refund r
            JOIN r.booking b
            LEFT JOIN b.customer c
            LEFT JOIN c.user u
            LEFT JOIN b.slotAllocations bsa
            LEFT JOIN bsa.bookingSlot bs
            LEFT JOIN bs.station st
            WHERE (:status IS NULL OR r.status = :status)
              AND (:year IS NULL OR YEAR(r.createdAt) = :year)
              AND (:month IS NULL OR MONTH(r.createdAt) = :month)
              AND (:stationId IS NULL OR st.id = :stationId)
              AND (:keyword IS NULL
                   OR c.firstName LIKE CONCAT('%', :keyword, '%')
                   OR c.lastName LIKE CONCAT('%', :keyword, '%')
                   OR u.phone LIKE CONCAT('%', :keyword, '%'))
            ORDER BY (CASE WHEN r.status = 'PENDING' THEN 0 ELSE 1 END) ASC, r.createdAt ASC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT r) FROM Refund r
            JOIN r.booking b
            LEFT JOIN b.customer c
            LEFT JOIN c.user u
            LEFT JOIN b.slotAllocations bsa
            LEFT JOIN bsa.bookingSlot bs
            LEFT JOIN bs.station st
            WHERE (:status IS NULL OR r.status = :status)
              AND (:year IS NULL OR YEAR(r.createdAt) = :year)
              AND (:month IS NULL OR MONTH(r.createdAt) = :month)
              AND (:stationId IS NULL OR st.id = :stationId)
              AND (:keyword IS NULL
                   OR c.firstName LIKE CONCAT('%', :keyword, '%')
                   OR c.lastName LIKE CONCAT('%', :keyword, '%')
                   OR u.phone LIKE CONCAT('%', :keyword, '%'))
            """)
    Page<Refund> findRefundsForAdmin(@Param("status") String status,
                                      @Param("year") Integer year,
                                      @Param("month") Integer month,
                                      @Param("stationId") Integer stationId,
                                      @Param("keyword") String keyword,
                                      Pageable pageable);

    /**
     * Số liệu tổng hợp theo filter đang áp dụng (AC9): tổng số dòng thỏa filter,
     * tổng số tiền đã hoàn (chỉ tính các dòng refundedAt khác NULL).
     */
    @Query("""
            SELECT COUNT(DISTINCT r),
                   COALESCE(SUM(CASE WHEN r.refundedAt IS NOT NULL THEN r.refundAmount ELSE 0 END), 0)
            FROM Refund r
            JOIN r.booking b
            LEFT JOIN b.customer c
            LEFT JOIN c.user u
            LEFT JOIN b.slotAllocations bsa
            LEFT JOIN bsa.bookingSlot bs
            LEFT JOIN bs.station st
            WHERE (:status IS NULL OR r.status = :status)
              AND (:year IS NULL OR YEAR(r.createdAt) = :year)
              AND (:month IS NULL OR MONTH(r.createdAt) = :month)
              AND (:stationId IS NULL OR st.id = :stationId)
              AND (:keyword IS NULL
                   OR c.firstName LIKE CONCAT('%', :keyword, '%')
                   OR c.lastName LIKE CONCAT('%', :keyword, '%')
                   OR u.phone LIKE CONCAT('%', :keyword, '%'))
            """)
    List<Object[]> getAdminSummary(@Param("status") String status,
                                    @Param("year") Integer year,
                                    @Param("month") Integer month,
                                    @Param("stationId") Integer stationId,
                                    @Param("keyword") String keyword);
}
