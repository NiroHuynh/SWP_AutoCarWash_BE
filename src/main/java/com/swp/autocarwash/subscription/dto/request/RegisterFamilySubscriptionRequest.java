package com.swp.autocarwash.subscription.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFamilySubscriptionRequest {

    @NotNull(message = "Family group id is required.")
    private Long familyGroupId;

    @NotNull(message = "Subscription plan id is required.")
    private Integer subscriptionPlanId;

}
