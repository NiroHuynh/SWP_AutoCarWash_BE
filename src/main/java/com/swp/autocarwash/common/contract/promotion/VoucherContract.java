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
    private Integer id;
    private String voucherCode;
    private Integer discountPercentage;
    private BigDecimal minOrderValue;

    public boolean isValid(BigDecimal subTotal) {
        return true; // simplified rule placeholder
    }
}
