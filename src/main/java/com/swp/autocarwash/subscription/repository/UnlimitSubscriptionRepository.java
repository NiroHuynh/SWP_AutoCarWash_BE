package com.swp.autocarwash.subscription.repository;

import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
