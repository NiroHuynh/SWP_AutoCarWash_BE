package com.swp.autocarwash.subscription.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUnlimitedSubscriptionRequest {

    @NotNull(message = "SUBSCRIPTION_PLAN_REQUIRED")
    private Integer subscriptionPlanId;

    @NotNull(message = "VEHICLE_REQUIRED")
    private Long vehicleId;

}
