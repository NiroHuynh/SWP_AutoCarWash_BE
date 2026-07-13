package com.swp.autocarwash.subscription.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilySubscriptionPlansResponse {

    private CurrentGroupInfo currentGroup;

    private List<FamilyPlanInfo> familyPlans;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentGroupInfo {

        private Long familyGroupId;

        private String groupName;

        private Integer slotsUsed;

        private SubscriptionInfo subscription;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscriptionInfo {

        private Long familySubscriptionId;

        private Integer subscriptionPlanId;

        private String planName;

        private String status;

        private LocalDate startDate;

        private LocalDate endDate;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FamilyPlanInfo {

        private Integer id;

        private String planName;

        private String description;

        private BigDecimal price;

        private Integer durationDays;

        private Integer maxVehicleCount;

        private Integer servicePackageId;

        private List<Integer> addonIds;

    }

}
