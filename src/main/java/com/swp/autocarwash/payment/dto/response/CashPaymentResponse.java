package com.swp.autocarwash.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Chức năng: Kết quả xử lý thanh toán tiền mặt — dùng để hiển thị biên nhận
 * cho staff tại quầy (tổng tiền cần thu, tiền khách đưa, tiền thừa, status mới).
 *
 * @author Ngọc
 * @version 1.0
 */
@Getter
@Builder
@AllArgsConstructor
public class CashPaymentResponse {

    /** Mã hoá đơn (BookingInvoice) vừa được tạo/cập nhật. */
    private Long invoiceId;

    /** Số tiền thực tế cần thu tại quầy (đã trừ tiền cọc nếu có). */
    private BigDecimal totalAmount;

    /** Số tiền mặt khách đưa. */
    private BigDecimal receivedAmount;

    /** Tiền thừa trả lại khách = receivedAmount - totalAmount. */
    private BigDecimal changeAmount;

    /** Trạng thái booking sau khi thanh toán (CHECK_OUT). */
    private String bookingStatus;

    /** Trạng thái giao dịch thanh toán (SUCCESS). */
    private String paymentStatus;
}
