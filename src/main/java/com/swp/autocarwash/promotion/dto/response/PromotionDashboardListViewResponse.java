package com.swp.autocarwash.promotion.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PromotionDashboardListViewResponse {

    private Integer id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private LocalDateTime createdAt;

    private List<StationInfo> stations;
    private List<TargetInfo> targets;
    private List<VoucherInfo> vouchers;

    @Data
    @Builder
    public static class StationInfo {
        private Integer stationId;
        private String stationName;
    }

    @Data
    @Builder
    public static class TargetInfo {
        private Integer targetId;
        private String targetName;
        private String targetCode;
    }

    @Data
    @Builder
    public static class VoucherInfo {
        private Long id;
        private String voucherCode;
        private Integer discountPercentage;
        private BigDecimal maxDiscountAmount;
        private BigDecimal minOrderValue;
        private Integer usageLimit;
        private Integer usedCount;
        private LocalDateTime startDate;
        private LocalDateTime expiryDate;
        private boolean reusable;
        private String status;
    }
}
