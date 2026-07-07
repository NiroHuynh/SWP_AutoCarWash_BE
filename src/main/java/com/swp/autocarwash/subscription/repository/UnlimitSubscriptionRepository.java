package com.swp.autocarwash.subscription.repository;

import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.Optional;

@Repository
public interface UnlimitSubscriptionRepository extends JpaRepository<UnlimitSubscription, Long> {

    @Query("SELECT u FROM UnlimitSubscription u JOIN FETCH u.subscriptionPlan " +
           "WHERE u.customer.id = :customerId AND u.vehicle.id = :vehicleId AND u.status = 'ACTIVE'")
    Optional<UnlimitSubscription> findActiveByCustomerAndVehicle(
            @Param("customerId") Long customerId,
            @Param("vehicleId") Long vehicleId);
    @Query("""
        SELECT sp.servicePackage.id
        FROM UnlimitSubscription us
        JOIN us.subscriptionPlan sp
        WHERE us.vehicle.id = :vehicleId
          AND us.status = 'ACTIVE'
          AND CURRENT_DATE BETWEEN us.startDate AND us.endDate
    """)
    Integer findActiveServicePackageIdByVehicleId(
            @Param("vehicleId") Long vehicleId
    );

    @Query("""
        SELECT COUNT(us) > 0
        FROM UnlimitSubscription us
        JOIN us.subscriptionPlan sp
        WHERE us.vehicle.id = :vehicleId
          AND sp.servicePackage.id = :servicePackageId
          AND us.status = 'ACTIVE'
          AND CURRENT_DATE BETWEEN us.startDate AND us.endDate
    """)
    boolean existsActiveUnlimitSubscription(
            @Param("vehicleId") Long vehicleId,
            @Param("servicePackageId") Integer servicePackageId
    );

    //Tìm bản ghi unlimit_subcription theo customerId+ vehicleId, đang ACTIVE và ngày hiện tại nằm trong khoảng
    // start, end date

    @Query("""
            SELECT s FROM UnlimitSubscription s 
                     WHERE s.customer.id = :customerId
                           AND s.vehicle.id = :vehicleId
                           AND s.status = :status
                           AND :checkDate BETWEEN s.startDate AND s.endDate
            """)

    UnlimitSubscription findActiveSubscription(
            @Param("customerId") Long customerId,
            @Param("vehicleId") Long vehicleId,
            @Param("status") SubscriptionStatus status,
            @Param("checkDate") LocalDate checkDate
    );

    //Overload tiện ích: tự lấy status bằng true và ngày hiện tại
    default UnlimitSubscription findActiveSubscriptionToday(Long customerId, Long vehicleId){
        return findActiveSubscription(customerId,vehicleId, SubscriptionStatus.ACTIVE, LocalDate.now());
    }

    /**
     * Kiểm tra xem khách hàng có đang sở hữu gói Subscription ACTIVE, còn hạn,
     * và gói đó áp dụng cho đúng mã gói dịch vụ chính (servicePackageId) đang chọn hay không.
     */
    @Query("SELECT COUNT(u) > 0 FROM UnlimitSubscription u " +
            "JOIN u.subscriptionPlan p " + //Đi xuyên qua bảng SubscriptionPlan
            "WHERE u.customer.id = :customerId " +
            "AND u.vehicle.id = :vehicleId " +
            "AND p.servicePackage.id = :servicePackageId " + //check khớp mã gói dịch vụ chính
            "AND u.status = :status " +
            "AND :currentDate BETWEEN u.startDate AND u.endDate")
    boolean hasActiveSubscription(@Param("customerId") Long customerId,
                                  @Param("vehicleId") Long vehicleId,
                                  @Param("servicePackageId") Integer servicePackageId,
                                  @Param("status") String status,
                                  @Param("currentDate") LocalDate currentDate);

    @Query("SELECT us FROM UnlimitSubscription us JOIN FETCH us.subscriptionPlan sp " +
            "WHERE us.vehicle.id = :vehicleId AND us.status = 'ACTIVE' AND sp.planType = 'UNLIMITED'")
    Optional<UnlimitSubscription> findActiveUnlimitedSubByVehicleId(@Param("vehicleId") Long vehicleId);

    @Query("SELECT u FROM UnlimitSubscription u WHERE " +
            "u.vehicle.id = :vehicleId " +
            "AND u.status = 'ACTIVE' " +
            "AND u.startDate <= :today AND u.endDate >= :today")
    Optional<UnlimitSubscription> findActiveSubscriptionByVehicle(
            @Param("vehicleId") Long vehicleId,
            @Param("today") LocalDate today
    );

    Optional<UnlimitSubscription> findByVehicleIdAndStatus(Long vehicleId, String status);

    @Query("SELECT COUNT(u) > 0 FROM UnlimitSubscription u WHERE u.vehicle.id = :vehicleId " +
            "AND u.status = 'ACTIVE' " +
            "AND :today BETWEEN u.startDate AND u.endDate")
    boolean hasActiveSubscription(@Param("vehicleId") Long vehicleId, @Param("today") LocalDate today);

    /** Gói unlimit đang ACTIVE của khách hàng, không giới hạn theo xe cụ thể — dùng cho màn "gói đang hoạt động". */
    @Query("SELECT u FROM UnlimitSubscription u JOIN FETCH u.subscriptionPlan " +
            "WHERE u.customer.id = :customerId " +
            "AND u.status = 'ACTIVE' " +
            "AND CURRENT_DATE BETWEEN u.startDate AND u.endDate")
    Optional<UnlimitSubscription> findActiveByCustomerId(@Param("customerId") Long customerId);
}
