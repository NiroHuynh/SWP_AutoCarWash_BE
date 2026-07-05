package com.swp.autocarwash.loyalty.dto.response;


import com.swp.autocarwash.loyalty.entity.enums.LoyaltyPointTransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyOverviewResponse {

    private Balance balance;

    private Progress progress;

    private List<Tier> tiers;

    private List<RecentTransaction> recentTransactions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Balance {

        private Integer totalPoints;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Progress {

        private String currentTierName;

        private String nextTierName;

        private Integer pointsToNextTier;

        private Integer progressPoints;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tier {

        private Integer id;

        private String tierName;

        private Integer minPoints;

        private BigDecimal pointMultiple;

        private Boolean isCurrent;

        private List<String> benefits;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentTransaction {

        private Long id;

        private LocalDateTime createdAt;

        /**
         * EARN | REDEEM
         */
        private LoyaltyPointTransactionType transactionType;

        /**
         * BOOKING | FAMILY | UNLIMITED
         */
        private String source;

        private Long subscriptionInvoiceId;

        private Long bookingId;

        private Integer points;

    }

}
