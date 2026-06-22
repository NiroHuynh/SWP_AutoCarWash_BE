package com.swp.autocarwash.subscription.adapter;

import com.swp.autocarwash.booking.port.FamilySubscriptionPort;
import com.swp.autocarwash.subscription.service.FamilySubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("pro")
public class FamilySubscriptionBookingAdapter implements FamilySubscriptionPort {

    private final FamilySubscriptionService familySubscriptionService;

    @Override
    public Integer getActiveServicePackageId(Long vehicelId) {
        return familySubscriptionService.getActiveServicePackageId(vehicelId);
    }
}
