package com.swp.autocarwash.booking.repository;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository truy vấn dữ liệu lịch đặt ({@link Booking}).
 *
 * <p>Sử dụng JOIN FETCH trong JPQL để tải sẵn các quan hệ lazy
 * ({@code vehicle}, {@code servicePackage}), tránh vấn đề N+1 query.
 * Không sort ở tầng này — việc sort theo {@code appointmentDate} và
 * {@code startTime} được thực hiện ở tầng service, vì {@code startTime}
 * không phải field của entity {@link Booking}.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Lấy danh sách booking của một khách hàng theo nhóm trạng thái, dùng chung
     * cho cả tab Upcoming và Past Services (việc sắp xếp do tầng service đảm nhiệm).
     *
     * @param customerId mã định danh của khách hàng cần truy vấn
     * @param statuses   danh sách trạng thái hợp lệ
     * @return danh sách {@link Booking} thỏa điều kiện, đã eager-fetch quan hệ liên quan
     */
    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN FETCH b.vehicle " +
           "JOIN FETCH b.servicePackage " +
           "WHERE b.customer.id = :customerId AND b.status IN :statuses")
    public List<Booking> findByCustomerIdAndStatuses(
            @Param("customerId") Long customerId,
            @Param("statuses") List<String> statuses
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
    public Optional<Booking> findDetailById(@Param("id") Long id);
// Optional như một cái hộp: nếu có hàng bên trong . ( booking ) thì lấy ra xài bình thường còn nếu
    //không có thì là hộp rỗng và bắt buộc phải ném exception


    //Vbinh
    //Tìm booking ở trạng thái CONFIRMED, check biển số xe và là booking của ngày hiện tại
    //1 xe chỉ có tối đa 1 booking CONFIRMED trong cùng 1 ngày

    //entity đang dùng fetch.lazy, tại đây join fetch ép k được dùng lazy trong đoạn lệnh này
    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.vehicle v
            WHERE v.licensePlate = :licensePlate
                  AND b.status = :status
                  AND b.appointmentDate = :appointmentDate
           """
    )

    Optional<Booking> findConfirmedBookingByLicensePlate(
        @Param("licensePlate") String licensePlate,
        @Param("status") String status,
        @Param("appointmentDate") LocalDate appointmentDate
    );

    //tự lấy status CONFIRMED và ngày hiện tại
    default Optional<Booking> findConfirmedBookingTodayByLicensePlate(String licensePlate){
        return findConfirmedBookingByLicensePlate(licensePlate, BookingStatus.CONFIRMED.toString(), LocalDate.now());
    }

    /**
     * Subtask 4.1: Tìm các Booking NO_SHOW trong ngày, có is_deposit_paid = true
     * (tức khách có đóng cọc, mới có gì để tịch thu), và chưa được Scheduler xử lý
     * tịch thu trước đó (deposit_confiscated_at IS NULL) - tránh xử lý trùng nếu
     * job vô tình chạy lại 2 lần trong cùng 1 ngày.
     * Dùng cho Cron Job tịch thu tiền cọc cuối ngày.
     */

//    @Query("""
//        SELECT b FROM Booking b
//        WHERE b.appointmentDate = :appointmentDate
//        AND b.status = :status
//        AND b.isDepositPaid = true
//        AND b.depositConfiscatedAt IS NULL
//        """)
//    List<Booking> findNoShowBookingsPendingDepositConfiscation(
//            @Param("appointmentDate") LocalDate appointmentDate,
//            @Param("status") String status
//    );

    @Query("SELECT b FROM Booking b WHERE b.appointmentDate = :appointmentDate AND b.status = :status AND b.isDepositPaid = true AND b.depositConfiscatedAt IS NULL")
    List<Booking> findNoShowBookingsPendingDepositConfiscation(
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("status") String status
    );

}
