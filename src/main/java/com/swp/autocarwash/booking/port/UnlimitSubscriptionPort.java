package com.swp.autocarwash.booking.port;

public interface UnlimitSubscriptionPort {
    Integer getActiveServicePackageId(Long vehicleId);
    boolean hasUnlimitSubscription(Long vehicleId, Integer servicePackageId);
}
