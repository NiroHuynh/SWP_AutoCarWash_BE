package com.swp.autocarwash.subscription.repository;

import com.swp.autocarwash.subscription.entity.FamilySubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
