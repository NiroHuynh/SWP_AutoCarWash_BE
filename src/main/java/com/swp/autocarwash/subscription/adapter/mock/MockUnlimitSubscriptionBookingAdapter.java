package com.swp.autocarwash.subscription.adapter.mock;

import com.swp.autocarwash.booking.port.UnlimitSubscriptionPort;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MockUnlimitSubscriptionBookingAdapter implements UnlimitSubscriptionPort {
    @Override
    public Integer getActiveServicePackageId(Long vehicelId) {
        return 0;
    }
}
