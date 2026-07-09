package com.swp.autocarwash.subscription.dto.response;

import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewalPaymentResponse {

    private Long subscriptionId;

    private Long invoiceId;

    private SubscriptionStatus status;

    private LocalDate startDate;

    private LocalDate endDate;

}
