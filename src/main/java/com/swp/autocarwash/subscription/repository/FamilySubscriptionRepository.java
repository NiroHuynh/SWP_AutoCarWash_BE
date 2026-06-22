package com.swp.autocarwash.subscription.repository;

import com.swp.autocarwash.subscription.entity.FamilySubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilySubscriptionRepository
        extends JpaRepository<FamilySubscription, Long> {


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
}
