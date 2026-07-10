package com.swp.autocarwash.payment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chức năng: Request xác nhận cọc thủ công khi webhook lỗi (admin/staff đối
 * chiếu sao kê rồi bấm xác nhận).
 *
 * @author Ngân
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManualDepositConfirmRequest {

    @NotNull
    private Long bookingId;

    /** Ghi chú đối soát, ví dụ mã giao dịch trên sao kê. */
    private String note;
}
