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
}
