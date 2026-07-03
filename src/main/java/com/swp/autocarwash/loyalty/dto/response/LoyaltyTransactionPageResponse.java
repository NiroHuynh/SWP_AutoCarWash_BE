package com.swp.autocarwash.loyalty.dto.response;

import com.swp.autocarwash.loyalty.entity.enums.LoyaltyPointTransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyTransactionPageResponse {

    private List<Transaction> content;

    private Integer page;

    private Integer size;

    private Long totalElements;

    private Integer totalPages;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Transaction {

        private Long id;

        private LocalDateTime createdAt;

        private LoyaltyPointTransactionType transactionType;

        private String source;

        private Long subscriptionInvoiceId;

        private Long bookingId;

        private Integer points;

    }

}
