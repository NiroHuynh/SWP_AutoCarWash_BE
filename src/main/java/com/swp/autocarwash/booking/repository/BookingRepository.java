package com.swp.autocarwash.booking.repository;

import com.swp.autocarwash.booking.entity.Booking;
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
     * hoặc rỗng nếu không tìm thấy
     */
    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.vehicle " +
            "JOIN FETCH b.servicePackage " +
            "LEFT JOIN FETCH b.checkInEmployee " +
            "WHERE b.id = :id")
    public Optional<Booking> findDetailById(@Param("id") Long id);
// Optional như một cái hộp: nếu có hàng bên trong . ( booking ) thì lấy ra xài bình thường còn nếu
    //không có thì là hộp rỗng và bắt buộc phải ném exception

    //    kiểm tra xem xe đó có booking vào ngày đặt chưa
    boolean existsByVehicleIdAndAppointmentDateAndStatusNot(
            Long vehicleId,
            LocalDate date,
            String status
    );


    //    trả ra những ngày mà vehicle đã sử dụng gói unlimit, tìm trên 1 khoảng thời gian
    @Query("""
                SELECT b.appointmentDate
                FROM Booking b
                JOIN UnlimitSubscription us
                    ON us.vehicle.id = b.vehicle.id
                JOIN SubscriptionPlan sp
                    ON sp.id = us.subscriptionPlan.id
                WHERE b.vehicle.id = :vehicleId
                AND sp.servicePackage.id = :servicePackageId
                AND b.servicePackage.id = :servicePackageId
                AND b.appointmentDate BETWEEN :fromDate AND :toDate
                AND b.appointmentDate 
                    BETWEEN us.startDate AND us.endDate
                AND b.status NOT IN ('CANCELED')
                AND us.status = 'ACTIVE'
                ORDER BY b.appointmentDate
            """)
    List<LocalDate> findUsedUnlimitBookingDates(
            @Param("vehicleId") Long vehicleId,
            @Param("servicePackageId") Integer servicePackageId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    //    trả ra những ngày mà vehicle đã sử dụng gói family, tìm trên 1 khoảng thời gian
    @Query("""
                SELECT b.appointmentDate AS bookingDate
                FROM Booking b
                JOIN FamilyMember fm
                    ON fm.vehicle.id = b.vehicle.id
                JOIN FamilySubscription fs
                    ON fs.familyGroup.id = fm.familyGroup.id
                JOIN SubscriptionPlan sp
                    ON sp.id = fs.subscriptionPlan.id
                WHERE b.vehicle.id = :vehicleId
                AND sp.servicePackage.id = :servicePackageId
                AND b.servicePackage.id = :servicePackageId
                AND b.appointmentDate BETWEEN :fromDate AND :toDate
                AND b.appointmentDate BETWEEN fs.startDate AND fs.endDate
                AND fs.status = 'ACTIVE'
                AND b.status NOT IN ('CANCELED')
                ORDER BY b.appointmentDate
            """)
    List<LocalDate> findFamilyUsedDates(
            Long vehicleId,
            Integer servicePackageId,
            LocalDate fromDate,
            LocalDate toDate
    );


}
