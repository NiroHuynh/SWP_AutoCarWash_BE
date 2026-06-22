package com.swp.autocarwash.subscription.adapter;

import com.swp.autocarwash.booking.port.UnlimitSubscriptionPort;
import com.swp.autocarwash.subscription.service.UnlimitSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("pro")
public class UnlimitSubscriptionBookingAdapter implements UnlimitSubscriptionPort {

    private final UnlimitSubscriptionService unlimitSubscriptionService;

    @Override
    public Integer getActiveServicePackageId(Long vehicelId) {
        return unlimitSubscriptionService.getActiveServicePackageId(vehicelId);
    }
}
