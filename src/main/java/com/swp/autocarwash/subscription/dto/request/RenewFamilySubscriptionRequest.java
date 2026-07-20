package com.swp.autocarwash.subscription.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewFamilySubscriptionRequest {

    @NotNull(message = "Subscription plan id is required.")
    private Integer subscriptionPlanId;

}