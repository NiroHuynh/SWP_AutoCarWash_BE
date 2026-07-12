package com.swp.autocarwash.subscription.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFamilySubscriptionResponse {

    private Long familySubscriptionId;

    private Long invoiceId;

    private String planName;

    private BigDecimal planPrice;

    private LocalDate startDate;

    private LocalDate endDate;

    /**
     * PENDING
     */
    private String status;

}
