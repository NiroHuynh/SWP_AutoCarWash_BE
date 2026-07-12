package com.swp.autocarwash.payment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Chức năng: Response tổng cho màn Payment Transaction History cho admin
 * (FE-61C-US-02) — gộp chung KPI summary + bảng chi tiết trong 1 lần gọi để
 * đảm bảo summary luôn khớp filter với bảng.
 *
 * @author Ngân
 * @version 1.0
 */
@Data
@Builder
public class PaymentTransactionHistoryResponse {

    private PaymentTransactionSummaryResponse summary;

    private List<PaymentTransactionResponse> transactions;
}
