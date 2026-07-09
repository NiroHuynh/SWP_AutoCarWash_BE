package com.swp.autocarwash.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Chức năng: Kết quả xác nhận cọc (thủ công) trả về cho admin/staff.
 *
 * @author Ngân
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositConfirmResponse {

    private Long bookingId;

    private String bookingStatus;

    private Boolean isDepositPaid;

    private BigDecimal depositAmount;
}
