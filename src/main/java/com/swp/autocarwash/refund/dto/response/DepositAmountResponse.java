package com.swp.autocarwash.refund.dto.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * Chức năng: Số tiền cọc cố định toàn hệ thống — hiển thị cho Customer trước khi
 * xác nhận hủy booking (form AC2), tránh phải tạo Refund mới biết được số tiền hoàn.
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositAmountResponse {

    private BigDecimal amount;
}
