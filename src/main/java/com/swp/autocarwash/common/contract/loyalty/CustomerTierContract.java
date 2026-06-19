package com.swp.autocarwash.common.contract.loyalty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTierContract {

    private Integer tierId;
    private String tierName;

    /**
     * số ngày được phép booking trước (AC logic)
     */
    private Integer bookingWindowDays;

    private Integer minPoints;
    private BigDecimal pointMultiple;
}