package com.swp.autocarwash.payment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chức năng: Request xác nhận thanh toán mua gói thủ công khi webhook lỗi.
 *
 * @author Ngân
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManualSubscriptionConfirmRequest {

    @NotNull
    private Long invoiceId;

    /** Ghi chú đối soát, ví dụ mã giao dịch trên sao kê. */
    private String note;
}
