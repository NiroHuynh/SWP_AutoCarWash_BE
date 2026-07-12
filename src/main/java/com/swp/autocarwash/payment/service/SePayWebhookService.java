package com.swp.autocarwash.payment.service;

import com.swp.autocarwash.payment.dto.request.ManualDepositConfirmRequest;
import com.swp.autocarwash.payment.dto.request.ManualSubscriptionConfirmRequest;
import com.swp.autocarwash.payment.dto.request.SePayWebhookRequest;
import com.swp.autocarwash.payment.dto.response.SubscriptionPaymentInitResponse;
import com.swp.autocarwash.payment.dto.response.DepositConfirmResponse;
import com.swp.autocarwash.payment.dto.response.WebhookLogResponse;

import java.util.List;

/**
 * Chức năng: Nghiệp vụ xử lý webhook giao dịch ngân hàng từ SePay để xác nhận
 * tiền cọc booking, kèm luồng xác nhận thủ công dự phòng khi webhook lỗi.
 *
 * @author Ngân
 * @version 1.0
 */
public interface SePayWebhookService {

    /**
     * Chức năng: Xử lý một webhook giao dịch từ SePay — idempotent theo mã giao
     * dịch, map booking từ nội dung chuyển khoản ("BK{bookingId}"), đối chiếu số
     * tiền cọc, xác nhận booking PENDING → CONFIRMED. Mọi giao dịch đều được lưu
     * vào payment_webhook_log; giao dịch lỗi (orphan, sai tiền, sai trạng thái)
     * được lưu với status tương ứng chứ không throw, để controller luôn trả 200
     * cho SePay khỏi retry.
     *
     * @param request payload webhook từ SePay
     */
    void handleWebhook(SePayWebhookRequest request);

    /**
     * Chức năng: Admin/staff xác nhận cọc thủ công khi webhook lỗi. Chỉ cho phép
     * với booking PENDING chưa đóng cọc.
     *
     * @param request bookingId + ghi chú đối soát
     * @return trạng thái booking sau xác nhận
     */
    DepositConfirmResponse manualConfirmDeposit(ManualDepositConfirmRequest request);

    /**
     * Chức năng: Admin/staff xác nhận thanh toán mua gói thủ công khi webhook
     * lỗi. Chỉ cho phép với hóa đơn đang PENDING.
     *
     * @param request invoiceId + ghi chú đối soát
     * @return trạng thái hóa đơn sau xác nhận
     */
    SubscriptionPaymentInitResponse manualConfirmSubscription(ManualSubscriptionConfirmRequest request);

    /**
     * Chức năng: Tra cứu lịch sử webhook theo trạng thái xử lý (ví dụ ORPHAN)
     * để admin đối soát thủ công; truyền null để lấy tất cả.
     */
    List<WebhookLogResponse> getWebhookLogs(String status);
}
