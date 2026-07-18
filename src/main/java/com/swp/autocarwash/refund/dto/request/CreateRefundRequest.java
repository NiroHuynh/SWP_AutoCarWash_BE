package com.swp.autocarwash.refund.dto.request;

import com.swp.autocarwash.refund.entity.enums.RefundMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Chức năng: Dữ liệu Customer nhập khi xác nhận hủy booking để nhận hoàn tiền (AC3).
 * Thiếu 1 trong các field bắt buộc → 400 field-level error (AC4).
 *
 * <p>{@code bankBin}/{@code accountNumber}/{@code accountHolder} chỉ bắt buộc khi
 * {@code refundMethod = BANK_TRANSFER} — được validate thủ công trong service vì
 * bean validation không hỗ trợ điều kiện phụ thuộc field khác.</p>
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

    @NotNull(message = "REFUND_METHOD_REQUIRED")
    private RefundMethod refundMethod;

    /** BIN ngân hàng khách chọn (map từ BankEnum) — bắt buộc nếu refundMethod = BANK_TRANSFER. */
    private String bankBin;

    private String accountNumber;

    private String accountHolder;
}
