package com.swp.autocarwash.subscription.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewFamilySubscriptionResponse {

    private Long familySubscriptionId;

    private String planName;

    /**
     * Số ngày của gói
     */
    private Integer planDuration;

    /**
     * ACTIVE
     */
    private String status;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer slotsUsed;

    private Integer maxSlots;

    private String inheritedTierName;

}