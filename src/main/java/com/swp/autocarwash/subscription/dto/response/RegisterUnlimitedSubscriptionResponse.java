package com.swp.autocarwash.subscription.dto.response;

import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUnlimitedSubscriptionResponse {

    private Long subscriptionId;

    private Long invoiceId;

    private SubscriptionStatus status;

}
