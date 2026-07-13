package com.swp.autocarwash.refund.dto.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * Chức năng: Số liệu tổng hợp theo filter đang áp dụng trên màn hình danh sách hoàn tiền (US-05 AC9).
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundSummaryResponse {

    /** Tổng số dòng Refund thỏa điều kiện filter. */
    private Long totalCount;
    /** Tổng số tiền đã hoàn — chỉ tính các dòng có refundedAt khác NULL, thỏa filter. */
    private BigDecimal totalRefundedAmount;
}
