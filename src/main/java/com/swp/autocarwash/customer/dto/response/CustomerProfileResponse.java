package com.swp.autocarwash.customer.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CustomerProfileResponse {

    private String message;
    private ProfileData data;

    @Data
    @Builder
    public static class ProfileData {
        private CustomerInfo customer;
        private TierInfo tier;
        private List<VehicleInfo> vehicles;
    }

    @Data
    @Builder
    public static class CustomerInfo {
        private Long id;
        private String firstName;
        private String lastName;
        private LocalDate birthday;
        private String email;
        private String phone;
        private String location;
    }

    @Data
    @Builder
    public static class TierInfo {
        private String currentTierName;
        private Integer currentPoints;
        private String nextTierName;
        private Integer nextTierMinPoints;
        private Integer pointsToNextTier;
    }

    @Data
    @Builder
    public static class VehicleInfo {
        private Long id;
        private String licensePlate;
        private String brandName;
        private String color;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ActiveSubscriptionInfo activeSubscription;
    }

    @Data
    @Builder
    public static class ActiveSubscriptionInfo {
        private String type;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean hasTransferred;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private LocalDate transferUnlockDate;
    }
}
