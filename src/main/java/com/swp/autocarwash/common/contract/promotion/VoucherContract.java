package com.swp.autocarwash.common.contract.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class VoucherContract {
    private Integer id;
    private String voucherCode;
    private Integer discountPercentage;
    private BigDecimal minOrderValue;
}
