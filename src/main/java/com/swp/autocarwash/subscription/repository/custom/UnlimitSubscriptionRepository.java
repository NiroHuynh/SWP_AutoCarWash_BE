package com.swp.autocarwash.subscription.repository.custom;

import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface UnlimitSubscriptionRepository extends JpaRepository<UnlimitSubscription,Long> {
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

}
