package com.swp.autocarwash.payment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Chức năng: KPI summary đầu màn Payment Transaction History cho admin
 * (FE-61C-US-02) — tính trên đúng tập kết quả đang được filter.
 *
 * @author Ngân
 * @version 1.0
 */
@Data
@Builder
public class PaymentTransactionSummaryResponse {

    private BigDecimal totalRevenue;

    private long totalCount;
}
