package com.swp.autocarwash.subscription.repository;

import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UnlimitSubscriptionRepository
        extends JpaRepository<UnlimitSubscription, Long> {


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

}
