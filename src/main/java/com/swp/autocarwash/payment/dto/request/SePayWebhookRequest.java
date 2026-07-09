package com.swp.autocarwash.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Chức năng: Payload chuẩn SePay gửi tới webhook khi có giao dịch ngân hàng.
 * Xem cấu trúc tại https://docs.sepay.vn/tich-hop-webhooks.html
 *
 * @author Ngân
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SePayWebhookRequest {

    /** ID giao dịch trên SePay — dùng làm khóa idempotency. */
    private Long id;

    /** Tên ngân hàng, ví dụ "MBBank". */
    private String gateway;

    /** Thời gian giao dịch phía ngân hàng, định dạng "yyyy-MM-dd HH:mm:ss". */
    private String transactionDate;

    private String accountNumber;

    /** Nội dung chuyển khoản — chứa mã "BK{bookingId}". */
    private String content;

    /** "in" = tiền vào, "out" = tiền ra. */
    private String transferType;

    private BigDecimal transferAmount;

    private BigDecimal accumulated;

    /** Mã tham chiếu của giao dịch ngân hàng. */
    private String referenceCode;

    private String description;

    private String code;

    private String subAccount;
}
