package com.swp.autocarwash.booking.repository;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.system.dto.response.PackageStatProjection;
import com.swp.autocarwash.system.dto.response.RevenueChartProjection;
import com.swp.autocarwash.system.dto.response.TierStatisticsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
     * Tìm các booking đang ở một trạng thái và được tạo trước một mốc thời gian —
     * dùng cho job tự hủy booking PENDING quá hạn chuyển khoản cọc.
     *
     * @param status trạng thái cần lọc (ví dụ PENDING)
     * @param cutoff mốc thời gian; chỉ lấy booking có createdAt trước mốc này
     */
    List<Booking> findByStatusAndCreatedAtBefore(String status, java.time.LocalDateTime cutoff);

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
            "JOIN FETCH b.servicePackage sp " +
            "JOIN FETCH sp.serviceCategory " +
            "LEFT JOIN FETCH b.checkInEmployee " +
            "LEFT JOIN FETCH b.customer c " +
            "LEFT JOIN FETCH c.customerTier " +
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

//    //tự lấy status CONFIRMED và ngày hiện tại
//    default Optional<Booking> findConfirmedBookingTodayByLicensePlate(String licensePlate) {
//        return findConfirmedBookingByLicensePlate(licensePlate, BookingStatus.CONFIRMED.toString(), LocalDate.now());
//    }

    @Query("SELECT b FROM Booking b " +
            "JOIN BookingSlotAllocation bsa ON bsa.booking.id = b.id " +
            "JOIN bsa.bookingSlot s " +
            "WHERE b.vehicle.licensePlate = :licensePlate " +
            "AND b.appointmentDate = :today " +
            "AND b.status = 'CONFIRMED' " +
            "ORDER BY s.startTime ASC LIMIT 1") //Lấy đơn có khung giờ sớm nhất để check-in trước
    Optional<Booking> findConfirmedBookingTodayByLicensePlate(
            @Param("licensePlate") String licensePlate,
            @Param("today") LocalDate today);

    /**
     * Subtask 4.1: Tìm các Booking NO_SHOW trong ngày, có is_deposit_paid = true
     * (tức khách có đóng cọc, mới có gì để tịch thu), và chưa được Scheduler xử lý
     * tịch thu trước đó (deposit_confiscated_at IS NULL) - tránh xử lý trùng nếu
     * job vô tình chạy lại 2 lần trong cùng 1 ngày.
     * Dùng cho Cron Job tịch thu tiền cọc cuối ngày.
     */

    @Query("SELECT b FROM Booking b WHERE b.appointmentDate = :appointmentDate AND b.status = :status AND b.isDepositPaid = true AND b.depositConfiscatedAt IS NULL")
    List<Booking> findNoShowBookingsPendingDepositConfiscation(
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("status") String status
    );

//    //tìm thông tin của xe trễ giờ booking nhưng đã quay lại trong ngày và vẫn sử dụng dịch vụ -> chuyển cọc sang đơn này
//    @Query("SELECT b FROM Booking b " +
//            "WHERE b.vehicle.licensePlate = :licensePlate " +
//            "AND b.appointmentDate = :appointmentDate " +
//            "AND b.status = :status " +
//            "AND b.isDepositPaid = true " +
//            "AND b.depositConfiscatedAt IS NULL")
//    Optional<Booking> findBookingToRescueDeposit(
//            @Param("licensePlate") String licensePlate,
//            @Param("appointmentDate") LocalDate appointmentDate,
//            @Param("status") String status
//    );

    @Query("SELECT b FROM Booking b " +
            "WHERE b.vehicle.licensePlate = :licensePlate " +
            "AND b.appointmentDate = :appointmentDate " +
            "AND b.status = :status " +
            "AND b.isDepositPaid = true " +
            "AND b.depositConfiscatedAt IS NULL " +
            "ORDER BY b.createdAt DESC LIMIT 1") //Luôn lấy đơn đặt gần nhất trong ngày để cứu cọc
    Optional<Booking> findBookingToRescueDeposit(
            @Param("licensePlate") String licensePlate,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("status") String status
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

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "JOIN BookingSlotAllocation bsa ON bsa.booking.id = b.id " + // Nối sang bảng phân bổ slot
            "WHERE b.vehicle.id = :vehicleId " +
            "AND b.appointmentDate = :date " +
            //CHẶN TẤT CẢ các trạng thái đang xử lý hoặc đã hoàn thành, chỉ bỏ qua trạng thái CANCELED
            "AND b.status IN ('PENDING', 'CONFIRMED', 'CHECK_IN', 'WASHING', 'COMPLETED') " +
            "AND bsa.bookingSlot.id IN :slotIds")
        // Dính vào bất kỳ slot nào trong danh sách đang chọn là chặn liền
    boolean existsByVehicleIdAndDateAndSlotIds(
            @Param("vehicleId") Long vehicleId,
            @Param("date") LocalDate date,
            @Param("slotIds") List<Long> slotIds
    );

    //Câu lệnh cập nhật hàng loạt trạng thái đơn bùng hẹn
    @Modifying
    @Query("UPDATE Booking b SET b.status = 'NO_SHOW' " +
            "WHERE b.appointmentDate = :date AND b.status = 'CONFIRMED'")
    int updateStatusToNoShowForExpiredBookings(@Param("date") LocalDate date);

    //Tìm danh sách các đơn SUBSCRIPTION bị bùng để tí nữa tính điểm phạt vi phạm
    @Query("SELECT b FROM Booking b WHERE b.appointmentDate = :date " +
            "AND b.status = 'CONFIRMED' AND b.bookingType = 'SUBSCRIPTION'")
    List<Booking> findUncheckedSubscriptionBookingsToday(@Param("date") LocalDate date);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.vehicle.id = :vehicleId " +
            "AND b.status IN ('PENDING', 'CONFIRMED', 'CHECK_IN', 'WASHING')")
    boolean hasActiveBooking(@Param("vehicleId") Long vehicleId);

    // Kiểm tra xe có booking dở dang hay không
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.vehicle.id = :vehicleId AND b.status IN :statuses")
    boolean existsByVehicleIdAndStatusIn(@Param("vehicleId") Long vehicleId, @Param("statuses") List<String> statuses);

    // Kiểm tra xem danh sách xe truyền vào có chiếc nào dính booking dở dang không
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.vehicle.id IN :vehicleIds AND b.status IN :statuses")
    boolean existsByVehicleIdInAndStatusIn(@Param("vehicleIds") List<Long> vehicleIds, @Param("statuses") List<String> statuses);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.customer.id = :customerId AND b.status = 'NO_SHOW'")
    long countNoShowByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId AND b.status NOT IN ('CANCELED','CHECK_OUT') ORDER BY b.appointmentDate DESC")
    List<Booking> findActiveBookingsByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT MAX(b.checkOutAt) FROM Booking b WHERE b.customer.id = :customerId AND b.status = 'CHECK_OUT'")
    LocalDateTime findLastCheckOutByCustomerId(@Param("customerId") Long customerId);

    /**
     * Kiểm tra khách hàng có ít nhất 1 booking đã CHECK_OUT tại đúng station
     * chỉ định — dùng để giới hạn staff chỉ xem được khách của chi nhánh mình.
     */
    @Query("""
            SELECT COUNT(b) > 0 FROM Booking b
            LEFT JOIN b.slotAllocations bsa LEFT JOIN bsa.bookingSlot bst
            WHERE b.customer.id = :customerId AND b.status = 'CHECK_OUT' AND bst.station.id = :stationId
            """)
    boolean existsCheckedOutBookingAtStation(
            @Param("customerId") Long customerId, @Param("stationId") Integer stationId);

    /**
     * Lịch sử đặt lịch của một khách hàng trên mọi chi nhánh, có filter theo
     * từ khóa phương tiện (biển số/hãng xe), loại dịch vụ, trạng thái booking
     * (giá trị thô của {@link BookingStatus}), chi nhánh, năm/tháng đặt lịch,
     * mặc định sort mới nhất trước (FE-US-09-04 AC1-AC6).
     */
    @Query(value = """
            SELECT DISTINCT b FROM Booking b
            JOIN FETCH b.vehicle v
            JOIN FETCH b.servicePackage sp
            JOIN FETCH sp.serviceCategory sc
            LEFT JOIN FETCH b.checkInEmployee ce
            LEFT JOIN b.slotAllocations bsa
            LEFT JOIN bsa.bookingSlot bs
            LEFT JOIN bs.station st
            LEFT JOIN st.commune cm
            LEFT JOIN cm.province pv
            WHERE b.customer.id = :customerId
              AND (:vehicleKeyword IS NULL
                   OR v.licensePlate LIKE CONCAT('%', :vehicleKeyword, '%')
                   OR v.brandName LIKE CONCAT('%', :vehicleKeyword, '%'))
              AND (:serviceCategoryId IS NULL OR sc.id = :serviceCategoryId)
              AND (:status IS NULL OR b.status = :status)
              AND (:stationId IS NULL OR st.id = :stationId)
              AND (:communeId IS NULL OR cm.id = :communeId)
              AND (:provinceId IS NULL OR pv.id = :provinceId)
              AND (:year IS NULL OR YEAR(b.appointmentDate) = :year)
              AND (:month IS NULL OR MONTH(b.appointmentDate) = :month)
            ORDER BY b.appointmentDate DESC, b.id DESC
            """,
            countQuery = """
                    SELECT COUNT(DISTINCT b) FROM Booking b
                    JOIN b.vehicle v JOIN b.servicePackage sp JOIN sp.serviceCategory sc
                    LEFT JOIN b.slotAllocations bsa LEFT JOIN bsa.bookingSlot bs LEFT JOIN bs.station st
                    LEFT JOIN st.commune cm LEFT JOIN cm.province pv
                    WHERE b.customer.id = :customerId
                      AND (:vehicleKeyword IS NULL
                           OR v.licensePlate LIKE CONCAT('%', :vehicleKeyword, '%')
                           OR v.brandName LIKE CONCAT('%', :vehicleKeyword, '%'))
                      AND (:serviceCategoryId IS NULL OR sc.id = :serviceCategoryId)
                      AND (:status IS NULL OR b.status = :status)
                      AND (:stationId IS NULL OR st.id = :stationId)
                      AND (:communeId IS NULL OR cm.id = :communeId)
                      AND (:provinceId IS NULL OR pv.id = :provinceId)
                      AND (:year IS NULL OR YEAR(b.appointmentDate) = :year)
                      AND (:month IS NULL OR MONTH(b.appointmentDate) = :month)
                    """)
    Page<Booking> findCustomerBookingHistory(
            @Param("customerId") Long customerId,
            @Param("vehicleKeyword") String vehicleKeyword,
            @Param("serviceCategoryId") Integer serviceCategoryId,
            @Param("status") String status,
            @Param("stationId") Integer stationId,
            @Param("communeId") Integer communeId,
            @Param("provinceId") Integer provinceId,
            @Param("year") Integer year,
            @Param("month") Integer month,
            Pageable pageable);

    /**
     * Danh sách toàn bộ booking của 1 chi nhánh cụ thể — dùng cho Staff/Admin
     * xem danh sách booking của station, filter theo trạng thái/khoảng ngày/
     * từ khóa (tên khách, SĐT, biển số), mặc định sort mới nhất trước.
     */
    @Query(value = """
            SELECT DISTINCT b FROM Booking b
            JOIN FETCH b.customer c JOIN FETCH c.user cu
            JOIN FETCH b.vehicle v
            JOIN FETCH b.servicePackage sp JOIN FETCH sp.serviceCategory sc
            LEFT JOIN FETCH b.checkInEmployee ce
            JOIN b.slotAllocations bsa JOIN bsa.bookingSlot bs
            WHERE bs.station.id = :stationId
              AND (:status IS NULL OR b.status = :status)
              AND (:fromDate IS NULL OR b.appointmentDate >= :fromDate)
              AND (:toDate IS NULL OR b.appointmentDate <= :toDate)
              AND (:keyword IS NULL
                   OR CONCAT(c.firstName, ' ', c.lastName) LIKE CONCAT('%', :keyword, '%')
                   OR cu.phone LIKE CONCAT('%', :keyword, '%')
                   OR v.licensePlate LIKE CONCAT('%', :keyword, '%'))
            ORDER BY b.appointmentDate DESC, b.id DESC
            """,
            countQuery = """
                    SELECT COUNT(DISTINCT b) FROM Booking b
                    JOIN b.customer c JOIN c.user cu JOIN b.vehicle v
                    JOIN b.slotAllocations bsa JOIN bsa.bookingSlot bs
                    WHERE bs.station.id = :stationId
                      AND (:status IS NULL OR b.status = :status)
                      AND (:fromDate IS NULL OR b.appointmentDate >= :fromDate)
                      AND (:toDate IS NULL OR b.appointmentDate <= :toDate)
                      AND (:keyword IS NULL
                           OR CONCAT(c.firstName, ' ', c.lastName) LIKE CONCAT('%', :keyword, '%')
                           OR cu.phone LIKE CONCAT('%', :keyword, '%')
                           OR v.licensePlate LIKE CONCAT('%', :keyword, '%'))
                    """)
    Page<Booking> findBookingsByStation(
            @Param("stationId") Integer stationId,
            @Param("status") String status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("keyword") String keyword,
            Pageable pageable);


    // ==================== TOTAL REVENUE ====================

    @Query("""
            SELECT COALESCE(SUM(b.totalAmount), 0)
            FROM Booking b
            WHERE b.status = 'CHECK_OUT'
              AND b.appointmentDate BETWEEN :fromDate AND :toDate
            """)
    BigDecimal getTotalRevenue(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    @Query("""
            SELECT COALESCE(SUM(b.totalAmount), 0)
            FROM Booking b
            WHERE b.status = 'CHECK_OUT'
            AND b.appointmentDate BETWEEN :fromDate AND :toDate
            AND EXISTS (
                SELECT 1
                FROM BookingSlotAllocation a
                WHERE a.booking = b
                AND a.bookingSlot.station.id = :stationId
            )
            """)
    BigDecimal getTotalRevenueByStation(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("stationId") Integer stationId
    );

    @Query("""
            SELECT COALESCE(SUM(b.totalAmount), 0)
            FROM Booking b
            WHERE b.status = 'CHECK_OUT'
            AND b.appointmentDate BETWEEN :fromDate AND :toDate
            AND EXISTS (
                SELECT 1
                FROM BookingSlotAllocation a
                WHERE a.booking = b
                AND a.bookingSlot.station.commune.province.id = :provinceId
            )
            """)
    BigDecimal getTotalRevenueByProvince(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("provinceId") Integer provinceId
    );

    @Query("""
            SELECT COALESCE(SUM(b.totalAmount), 0)
            FROM Booking b
            WHERE b.status = 'CHECK_OUT'
              AND b.appointmentDate BETWEEN :fromDate AND :toDate
              AND EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                      AND a.bookingSlot.station.commune.id = :communeId
              )
            """)
    BigDecimal getTotalRevenueByCommune(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("communeId") Integer communeId
    );

    // ==================== TOTAL BOOKINGS ====================

    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.status <> 'CANCELED'
              AND b.appointmentDate BETWEEN :fromDate AND :toDate
            """)
    Long getTotalBookings(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.status <> 'CANCELED'
            AND b.appointmentDate BETWEEN :fromDate AND :toDate
            AND EXISTS (
                SELECT 1
                FROM BookingSlotAllocation a
                WHERE a.booking = b
                AND a.bookingSlot.station.id = :stationId
            )
            """)
    Long getTotalBookingsByStation(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("stationId") Integer stationId
    );

    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.status <> 'CANCELED'
            AND b.appointmentDate BETWEEN :fromDate AND :toDate
            AND EXISTS (
                SELECT 1
                FROM BookingSlotAllocation a
                WHERE a.booking = b
                AND a.bookingSlot.station.commune.province.id = :provinceId
            )
            """)
    Long getTotalBookingsByProvince(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("provinceId") Integer provinceId
    );

    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.status <> 'CANCELED'
              AND b.appointmentDate BETWEEN :fromDate AND :toDate
              AND EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                      AND a.bookingSlot.station.commune.id = :communeId
              )
            """)
    Long getTotalBookingsByCommune(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("communeId") Integer communeId
    );

    // ==================== TOTAL CUSTOMERS ====================

    @Query("""
            SELECT COUNT(DISTINCT b.customer.id)
            FROM Booking b
            WHERE b.appointmentDate BETWEEN :fromDate AND :toDate
            """)
    Long getTotalCustomers(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    @Query("""
            SELECT COUNT(DISTINCT b.customer.id)
            FROM Booking b
            WHERE b.appointmentDate BETWEEN :fromDate AND :toDate
            AND EXISTS (
                SELECT 1
                FROM BookingSlotAllocation a
                WHERE a.booking = b
                AND a.bookingSlot.station.id = :stationId
            )
            """)
    Long getTotalCustomersByStation(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("stationId") Integer stationId
    );

    @Query("""
            SELECT COUNT(DISTINCT b.customer.id)
            FROM Booking b
            WHERE b.appointmentDate BETWEEN :fromDate AND :toDate
            AND EXISTS (
                SELECT 1
                FROM BookingSlotAllocation a
                WHERE a.booking = b
                AND a.bookingSlot.station.commune.province.id = :provinceId
            )
            """)
    Long getTotalCustomersByProvince(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("provinceId") Integer provinceId
    );

    @Query("""
            SELECT COUNT(DISTINCT b.customer.id)
            FROM Booking b
            WHERE b.appointmentDate BETWEEN :fromDate AND :toDate
              AND EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                      AND a.bookingSlot.station.commune.id = :communeId
              )
            """)
    Long getTotalCustomersByCommune(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("communeId") Integer communeId
    );

    @Query("""
            SELECT
                MONTH(b.appointmentDate) as label,
                COALESCE(SUM(b.totalAmount),0) as value
            FROM Booking b
            WHERE b.status = 'CHECK_OUT'
            AND b.appointmentDate BETWEEN :fromDate AND :toDate
            
            AND (
                :stationId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                    AND a.bookingSlot.station.id = :stationId
                )
            )
            
            AND (
                :provinceId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                    AND a.bookingSlot.station.commune.province.id = :provinceId
                )
            )
            
            AND (
                :communeId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                    AND a.bookingSlot.station.commune.id = :communeId
                )
            )
            
            GROUP BY MONTH(b.appointmentDate)
            ORDER BY MONTH(b.appointmentDate)
            """)
    List<RevenueChartProjection> getRevenueByMonth(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("stationId") Integer stationId,
            @Param("provinceId") Integer provinceId,
            @Param("communeId") Integer communeId
    );

    @Query("""
            SELECT
                DAY(b.appointmentDate) as label,
                COALESCE(SUM(b.totalAmount),0) as value
            FROM Booking b
            WHERE b.status = 'CHECK_OUT'
            AND b.appointmentDate BETWEEN :fromDate AND :toDate
            
            AND (
                :stationId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                    AND a.bookingSlot.station.id = :stationId
                )
            )
            
            AND (
                :provinceId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                    AND a.bookingSlot.station.commune.province.id = :provinceId
                )
            )
            
            AND (
                :communeId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                    AND a.bookingSlot.station.commune.id = :communeId
                )
            )
            
            GROUP BY DAY(b.appointmentDate)
            ORDER BY DAY(b.appointmentDate)
            """)
    List<RevenueChartProjection> getRevenueByDay(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("stationId") Integer stationId,
            @Param("provinceId") Integer provinceId,
            @Param("communeId") Integer communeId
    );

    @Query("""
            SELECT
                QUARTER(b.appointmentDate) as label,
                COALESCE(SUM(b.totalAmount),0) as value
            FROM Booking b
            WHERE b.status = 'CHECK_OUT'
            AND b.appointmentDate BETWEEN :fromDate AND :toDate
            
            AND (
                :stationId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                    AND a.bookingSlot.station.id = :stationId
                )
            )
            
            AND (
                :provinceId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                    AND a.bookingSlot.station.commune.province.id = :provinceId
                )
            )
            
            AND (
                :communeId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                    AND a.bookingSlot.station.commune.id = :communeId
                )
            )
            GROUP BY QUARTER(b.appointmentDate)
            ORDER BY QUARTER(b.appointmentDate)
            """)
    List<RevenueChartProjection> getRevenueByQuarter(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("stationId") Integer stationId,
            @Param("provinceId") Integer provinceId,
            @Param("communeId") Integer communeId
    );

    @Query(value = """
            SELECT
                HOUR(b.check_out_at) AS label,
                COALESCE(SUM(b.total_amount),0) AS value
            FROM booking b
            WHERE b.status = 'CHECK_OUT'
            AND DATE(b.check_out_at) = :date
            
            AND (
                :stationId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM booking_slot_allocation a
                    JOIN booking_slot s
                        ON s.id = a.booking_slot_id
                    WHERE a.booking_id = b.id
                    AND s.station_id = :stationId
                )
            )
            
            AND (
                :provinceId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM booking_slot_allocation a
                    JOIN booking_slot s
                        ON s.id = a.booking_slot_id
                    JOIN station st
                        ON st.id = s.station_id
                    JOIN commune c
                        ON c.id = st.commune_id
                    WHERE a.booking_id = b.id
                    AND c.province_id = :provinceId
                )
            )
            
            AND (
                :communeId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM booking_slot_allocation a
                    JOIN booking_slot s
                        ON s.id = a.booking_slot_id
                    JOIN station st
                        ON st.id = s.station_id
                    WHERE a.booking_id = b.id
                    AND st.commune_id = :communeId
                )
            )
            GROUP BY HOUR(b.check_out_at)
            ORDER BY HOUR(b.check_out_at)
            """, nativeQuery = true)
    List<RevenueChartProjection> getRevenueByHour(
            @Param("date") LocalDate date,
            @Param("stationId") Integer stationId,
            @Param("provinceId") Integer provinceId,
            @Param("communeId") Integer communeId
    );

    @Query("""
            SELECT COALESCE(SUM(b.totalAmount),0)
            FROM Booking b
            WHERE b.status = 'CHECK_OUT'
            AND b.appointmentDate BETWEEN :fromDate AND :toDate
            
            AND (
                :stationId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                    AND a.bookingSlot.station.id = :stationId
                )
            )
            
            AND (
                :provinceId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                    AND a.bookingSlot.station.commune.province.id = :provinceId
                )
            )
            AND (
                :communeId IS NULL
                OR EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                    AND a.bookingSlot.station.commune.id = :communeId
                )
            )
            """)
    BigDecimal getRevenueTotal(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("stationId") Integer stationId,
            @Param("provinceId") Integer provinceId,
            @Param("communeId") Integer communeId
    );

    @Query("""
            SELECT
                sp.name AS packageName,
                COUNT(b.id) AS bookingCount
            FROM Booking b
            JOIN b.servicePackage sp
            WHERE b.status <> 'CANCELED'
              AND b.appointmentDate BETWEEN :fromDate AND :toDate
            
              AND (
                    :stationId IS NULL
                    OR EXISTS (
                        SELECT 1
                        FROM BookingSlotAllocation bsa
                        WHERE bsa.booking = b
                          AND bsa.bookingSlot.station.id = :stationId
                    )
              )
            
              AND (
                    :provinceId IS NULL
                    OR EXISTS (
                        SELECT 1
                        FROM BookingSlotAllocation bsa
                        WHERE bsa.booking = b
                          AND bsa.bookingSlot.station.commune.province.id = :provinceId
                    )
              )
              AND (
                  :communeId IS NULL
                  OR EXISTS (
                      SELECT 1
                      FROM BookingSlotAllocation a
                      WHERE a.booking = b
                      AND a.bookingSlot.station.commune.id = :communeId
                  )
              )
            GROUP BY sp.id, sp.name
            ORDER BY COUNT(b.id) DESC
            """)
    List<PackageStatProjection> getPackageStatistics(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("stationId") Integer stationId,
            @Param("provinceId") Integer provinceId,
            @Param("communeId") Integer communeId
    );

    @Query("""
            SELECT COUNT(b.id)
            FROM Booking b
            WHERE b.status <> 'CANCELED'
              AND b.appointmentDate BETWEEN :fromDate AND :toDate
            
              AND (
                    :stationId IS NULL
                    OR EXISTS (
                        SELECT 1
                        FROM BookingSlotAllocation bsa
                        WHERE bsa.booking = b
                          AND bsa.bookingSlot.station.id = :stationId
                    )
              )
            
              AND (
                    :provinceId IS NULL
                    OR EXISTS (
                        SELECT 1
                        FROM BookingSlotAllocation bsa
                        WHERE bsa.booking = b
                          AND bsa.bookingSlot.station.commune.province.id = :provinceId
                    )
              )
              AND (
                  :communeId IS NULL
                  OR EXISTS (
                      SELECT 1
                      FROM BookingSlotAllocation a
                      WHERE a.booking = b
                      AND a.bookingSlot.station.commune.id = :communeId
                  )
              )
            """)
    Long getTotalBookingForPackageStatistic(
            LocalDate fromDate,
            LocalDate toDate,
            Integer stationId,
            Integer provinceId,
            @Param("communeId") Integer communeId
    );

    boolean existsByVehicleIdAndServicePackageIdAndAppointmentDateAndStatusNot(
            Long vehicleId,
            Integer servicePackageId,
            LocalDate appointmentDate,
            String status
    );

    @Query("""
            SELECT
                ct.tierName AS tier,
                COUNT(DISTINCT c.id) AS customerCount
            FROM Customer c
            JOIN Booking b
                ON b.customer = c
            LEFT JOIN c.customerTier ct
            WHERE b.appointmentDate BETWEEN :fromDate AND :toDate
              AND b.status <> 'CANCELED'
              AND EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                      AND a.bookingSlot.station.id = :stationId
              )
            GROUP BY ct.tierName
            """)
    List<TierStatisticsProjection> getTierStatisticsByStation(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("stationId") Integer stationId
    );

    @Query("""
            SELECT
                ct.tierName AS tier,
                COUNT(DISTINCT c.id) AS customerCount
            FROM Customer c
            JOIN Booking b
                ON b.customer = c
            LEFT JOIN c.customerTier ct
            WHERE b.appointmentDate BETWEEN :fromDate AND :toDate
              AND b.status <> 'CANCELED'
              AND EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                      AND a.bookingSlot.station.commune.id = :communeId
              )
            GROUP BY ct.tierName
            """)
    List<TierStatisticsProjection> getTierStatisticsByCommune(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("communeId") Integer communeId
    );

    @Query("""
            SELECT
                ct.tierName AS tier,
                COUNT(DISTINCT c.id) AS customerCount
            FROM Customer c
            JOIN Booking b
                ON b.customer = c
            LEFT JOIN c.customerTier ct
            WHERE b.appointmentDate BETWEEN :fromDate AND :toDate
              AND b.status <> 'CANCELED'
              AND EXISTS (
                    SELECT 1
                    FROM BookingSlotAllocation a
                    WHERE a.booking = b
                      AND a.bookingSlot.station.commune.province.id = :provinceId
              )
            GROUP BY ct.tierName
            """)
    List<TierStatisticsProjection> getTierStatisticsByProvince(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("provinceId") Integer provinceId
    );

    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
        FROM Booking b
        WHERE b.vehicle.id = :vehicleId
          AND b.servicePackage.id = :servicePackageId
          AND b.appointmentDate = :appointmentDate
          AND b.status IN (
                'PENDING',
                'CONFIRMED',
                'CHECKED_IN',
                'WASHING',
                'COMPLETED'
          )
    """)
    boolean existsEffectiveBooking(
            @Param("vehicleId") Long vehicleId,
            @Param("servicePackageId") Integer servicePackageId,
            @Param("appointmentDate") LocalDate appointmentDate
    );

}
