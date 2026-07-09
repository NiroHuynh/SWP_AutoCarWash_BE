package com.swp.autocarwash.subscription.repository;

import com.swp.autocarwash.customer.entity.FamilyMember;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface FamilySubscriptionRepository extends JpaRepository<FamilySubscription, Long> {

    @Query("SELECT fs FROM FamilySubscription fs JOIN FETCH fs.subscriptionPlan " +
           "WHERE fs.familyGroup IN (" +
           "  SELECT fm.familyGroup FROM FamilyMember fm " +
           "  WHERE fm.customer.id = :customerId AND fm.vehicle.id = :vehicleId" +
           ") AND fs.status = 'ACTIVE'")
    Optional<FamilySubscription> findActiveByCustomerAndVehicle(
            @Param("customerId") Long customerId,
            @Param("vehicleId") Long vehicleId);
    @Query("""
                SELECT sp.servicePackage.id
                FROM FamilySubscription fs
                JOIN fs.subscriptionPlan sp
                JOIN fs.familyGroup fg
                JOIN FamilyMember fm 
                    ON fm.familyGroup.id = fg.id
                WHERE fm.vehicle.id = :vehicleId
                  AND fs.status = 'ACTIVE'
                  AND CURRENT_DATE BETWEEN fs.startDate AND fs.endDate
            """)
    Integer findActiveServicePackageIdByVehicleId(
            @Param("vehicleId") Long vehicleId
    );

    @Query("""
        SELECT COUNT(fs) > 0
        FROM FamilySubscription fs
        JOIN fs.subscriptionPlan sp
        JOIN fs.familyGroup fg
        JOIN FamilyMember fm
            ON fm.familyGroup.id = fg.id
        WHERE fm.vehicle.id = :vehicleId
          AND sp.servicePackage.id = :servicePackageId
          AND fs.status = 'ACTIVE'
          AND CURRENT_DATE BETWEEN fs.startDate AND fs.endDate
    """)
    boolean existsActiveFamilySubscription(
            @Param("vehicleId") Long vehicleId,
            @Param("servicePackageId") Integer servicePackageId
    );

    //KIỂM TRA XE CÓ THUỘC GÓI FAMILY ĐANG ACTIVE KHÔNG
    @Query("SELECT fs FROM FamilySubscription fs " +
            "JOIN FETCH fs.subscriptionPlan sp " +
            "JOIN fs.familyGroup fg " +
            "JOIN FamilyMember fm ON fm.familyGroup.id = fg.id " + // Bắc cầu sang bảng family_member
            "WHERE fm.vehicle.id = :vehicleId " +
            "AND fs.status = 'ACTIVE' " +
            "AND sp.planType = 'FAMILY'")
    Optional<FamilySubscription> findActiveFamilySubByVehicleId(@Param("vehicleId") Long vehicleId);

    @Query("SELECT fs FROM FamilySubscription fs " +
            "JOIN FamilyMember fm ON fs.familyGroup.id = fm.familyGroup.id " +
            "WHERE fm.vehicle.id = :vehicleId " +
            "AND fs.status = 'ACTIVE' " +
            "AND fs.startDate <= :today AND fs.endDate >= :today")
    Optional<FamilySubscription> findActiveFamilySubscriptionByVehicle(
            @Param("vehicleId") Long vehicleId,
            @Param("today") LocalDate today
    );

    /** Gói family đang ACTIVE của khách hàng, không giới hạn theo xe cụ thể — dùng cho màn "gói đang hoạt động". */
    @Query("SELECT fs FROM FamilySubscription fs JOIN FETCH fs.subscriptionPlan " +
            "WHERE fs.familyGroup IN (" +
            "  SELECT fm.familyGroup FROM FamilyMember fm WHERE fm.customer.id = :customerId" +
            ") AND fs.status = 'ACTIVE' " +
            "AND CURRENT_DATE BETWEEN fs.startDate AND fs.endDate")
    Optional<FamilySubscription> findActiveByCustomerId(@Param("customerId") Long customerId);
}
