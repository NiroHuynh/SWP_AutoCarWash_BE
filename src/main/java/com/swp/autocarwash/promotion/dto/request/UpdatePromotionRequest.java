package com.swp.autocarwash.promotion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UpdatePromotionRequest {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> stationIds;
    private List<Integer> targetIds;
    private List<VoucherUpdateSubRequest> vouchers;

    @Data
    public static class VoucherUpdateSubRequest {
        private Long id; // Bằng null -> Tạo mới, Có giá trị -> Cập nhật
        private String voucherCode;
        private Integer discountPercentage;
        private BigDecimal maxDiscountAmount;
        private BigDecimal minOrderValue;
        private Integer usageLimit;
        private Boolean reusable;
    }
}
