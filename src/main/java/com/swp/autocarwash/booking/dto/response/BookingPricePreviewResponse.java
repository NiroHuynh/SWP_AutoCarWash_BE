package com.swp.autocarwash.booking.dto.response;



import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingPricePreviewResponse {

    private String currency;

    private PriceBreakdown breakdown;

    private AppliedVoucher appliedVoucher;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PriceBreakdown {
        private BigDecimal servicePrice;
        private BigDecimal addonPrice;
        private BigDecimal subTotal;

        private String voucherCode;
        private BigDecimal voucherDiscount;

        private BigDecimal finalTotal;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AppliedVoucher {
        private boolean valid;
        private Integer discountPercentage;
    }
}
