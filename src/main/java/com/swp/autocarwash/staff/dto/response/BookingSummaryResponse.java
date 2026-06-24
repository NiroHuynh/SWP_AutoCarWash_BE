package com.swp.autocarwash.staff.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingSummaryResponse {
    private BigDecimal rawAmount;
    private BigDecimal packageDiscount;
    private BigDecimal penaltyDeposit;
    private BigDecimal transferredCredit;
    private BigDecimal remainingBalance;
    private String systemNotice;
    private boolean isActionBlock;
}
//rawAmount , packageDiscount (Khấu trừ gói) ,
// penaltyDeposit (Cọc phạt 20k) , transferredCredit (Cọc cũ chuyển sang),
// và remainingBalance (Tổng tiền cần thu lúc lấy xe).
