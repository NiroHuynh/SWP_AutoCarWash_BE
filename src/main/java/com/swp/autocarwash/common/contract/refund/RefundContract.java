package com.swp.autocarwash.common.contract.refund;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Chức năng: Snapshot dữ liệu của một Refund cần cho việc hiển thị trên booking
 * card/detail, truyền qua ranh giới module refund → booking mà không lộ JPA entity.
 *
 * @author KimNgan
 * @version 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundContract {

    private String bankName;
    private String accountNumber;
    private BigDecimal amount;
    /** {@code null} nếu Admin chưa xác nhận đã chuyển khoản (refund đang PENDING). */
    private Instant refundedAt;
}
