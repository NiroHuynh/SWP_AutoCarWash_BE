package com.swp.autocarwash.refund.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Chức năng: Dữ liệu Customer nhập khi xác nhận hủy booking để nhận hoàn tiền (AC3).
 * Thiếu 1 trong các field bắt buộc → 400 field-level error (AC4).
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRefundRequest {

    @NotNull(message = "REFUND_BOOKING_ID_REQUIRED")
    private Long bookingId;

    /** BIN ngân hàng khách chọn (map từ BankEnum). */
    @NotBlank(message = "REFUND_BANK_REQUIRED")
    private String bankBin;

    @NotBlank(message = "REFUND_ACCOUNT_NUMBER_REQUIRED")
    private String accountNumber;

    @NotBlank(message = "REFUND_ACCOUNT_HOLDER_REQUIRED")
    private String accountHolder;
}
