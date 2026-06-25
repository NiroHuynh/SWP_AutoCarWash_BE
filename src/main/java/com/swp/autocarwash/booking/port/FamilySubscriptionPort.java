package com.swp.autocarwash.booking.port;

public interface FamilySubscriptionPort {
    Integer getActiveServicePackageId(Long vehicelId);
    boolean hasFamilySubscription(Long vehicle, Integer servicePackageId);
}
