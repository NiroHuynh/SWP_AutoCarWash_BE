package com.swp.autocarwash.promotion.dto.request;

import com.swp.autocarwash.promotion.entity.Voucher;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreatePromotionVoucherRequest {
    private Integer configMode; // 1: Direct, 2: Campaign-Linked, 3: Standalone

    // ====== CHIẾN DỊCH (MODE 1 & 2) ======
    private String campaignName;
    private LocalDate campaignStartDate;
    private LocalDate campaignEndDate;
    private List<Integer> targetCustomerTierIds; // Mảng ID áp dụng (ví dụ: 1, 2)
    //CHỨA NHIỀU VOUCHER (Dùng cho Chế độ 2)
    private List<VoucherSubRequest> vouchers;

    // ====== VOUCHER (MODE 2 & 3) ======
    private String voucherCode;
    private String discountType; // FIXED hoặc PERCENTAGE
    private BigDecimal discountValue; // Số tiền giảm cố định HOẶC số phần trăm (%)
    private BigDecimal minOrderValue;
    private BigDecimal maxDiscountAmount; // Trần giảm giá (chỉ dùng cho PERCENTAGE)
    private Integer usageLimit;
    private Boolean reusable;

    // ====== RIÊNG VOUCHER ĐỘC LẬP (MODE 3) ======
    private LocalDate voucherStartDate;
    private LocalDate voucherEndDate;

    private List<Integer> stationIds;

    //CLASS CON ĐỂ HỨNG MẢNG VOUCHERS
    @Data
    public static class VoucherSubRequest {
        private String voucherCode;
        private String discountType; // FIXED hoặc PERCENTAGE
        private BigDecimal discountValue;
        private BigDecimal maxDiscountAmount;
        private BigDecimal minOrderValue;
        private Integer usageLimit;
        private Boolean reusable;
    }
}
