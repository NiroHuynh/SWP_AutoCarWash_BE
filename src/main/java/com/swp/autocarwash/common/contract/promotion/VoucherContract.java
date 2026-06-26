package com.swp.autocarwash.common.contract.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherContract {
    private Long id;
    private String voucherCode;
    private Integer discountPercentage;
    private BigDecimal minOrderValue;
    private boolean valid;


    public VoucherContract(Long id, String voucherCode, Integer discountPercentage, BigDecimal minOrderValue) {
        this.id = id;
        this.voucherCode = voucherCode;
        this.discountPercentage = discountPercentage;
        this.minOrderValue = minOrderValue;
    }

    public boolean isValid(BigDecimal subTotal) {
        return valid;
    }
}
