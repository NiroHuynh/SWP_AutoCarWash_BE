package com.swp.autocarwash.subscription.adapter.mock;

import com.swp.autocarwash.booking.port.FamilySubscriptionPort;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MockFamilySubscriptionBookingAdapter implements FamilySubscriptionPort {
    @Override
    public Integer getActiveServicePackageId(Long vehicelId) {
        return null;
    }

    @Override
    public boolean hasFamilySubscription(Long vehicle, Integer servicePackageId) {
        return false;
    }
}
