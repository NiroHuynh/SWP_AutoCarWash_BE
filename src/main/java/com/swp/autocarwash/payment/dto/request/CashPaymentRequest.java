package com.swp.autocarwash.payment.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Chức năng: Request thanh toán tiền mặt tại quầy cho một booking.
 *
 * @author Ngọc
 * @version 1.0
 */
@Getter
@Setter
public class CashPaymentRequest {

    /**
     * Mã định danh booking cần thu tiền (phải đang ở trạng thái COMPLETED).
     */
    @NotNull
    private Long bookingId;

    /**
     * Số tiền mặt khách đưa cho staff — dùng để tính tiền thừa (changeAmount).
     */
    @NotNull
    @Positive
    private BigDecimal receivedAmount;
}
