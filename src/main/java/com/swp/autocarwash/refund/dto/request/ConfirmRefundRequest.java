package com.swp.autocarwash.refund.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Chức năng: Dữ liệu Admin nhập khi xác nhận đã hoàn tiền xong (US-05 AC3, AC4).
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRefundRequest {

    @NotBlank(message = "REFUND_TRANSACTION_CODE_REQUIRED")
    private String transactionCode;
}
