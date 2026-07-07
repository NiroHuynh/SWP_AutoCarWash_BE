package com.swp.autocarwash.payment.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.payment.dto.request.CashPaymentRequest;
import com.swp.autocarwash.payment.dto.request.ManualDepositConfirmRequest;
import com.swp.autocarwash.payment.dto.request.ManualSubscriptionConfirmRequest;
import com.swp.autocarwash.payment.dto.response.SubscriptionPaymentInitResponse;
import com.swp.autocarwash.payment.dto.response.CashPaymentResponse;
import com.swp.autocarwash.payment.dto.response.DepositConfirmResponse;
import com.swp.autocarwash.payment.dto.response.WebhookLogResponse;
import com.swp.autocarwash.payment.service.PaymentService;
import com.swp.autocarwash.payment.service.SePayWebhookService;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Chức năng: Controller xử lý thanh toán tại quầy — staff thu tiền mặt khi
 * khách check-out.
 *
 * <p>Base URL: {@code /api/payments}</p>
 *
 * @author Ngọc
 * @version 1.0
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final SePayWebhookService sePayWebhookService;

    /**
     * Chức năng: Thu tiền mặt cho một booking đã COMPLETED — tạo/cập nhật hoá
     * đơn + giao dịch thanh toán, chuyển booking sang CHECK_OUT.
     *
     * <p><b>Ví dụ:</b> {@code POST /api/payments/cash}</p>
     *
     * @param request bookingId + receivedAmount (số tiền khách đưa)
     * @return {@code 200 OK} với {@link CashPaymentResponse}
     */
    @PostMapping("/cash")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ApiResponse<CashPaymentResponse>> processCashPayment(
            @Valid @RequestBody CashPaymentRequest request) {

        CashPaymentResponse result = paymentService.processCashPayment(request);

        return ResponseEntity.ok(
                ApiResponse.success("Thanh toán tiền mặt thành công", result)
        );
    }

    /**
     * Chức năng: Xác nhận cọc thủ công khi webhook SePay lỗi (fallback) —
     * admin/staff đối chiếu sao kê rồi bấm xác nhận. Chỉ áp dụng cho booking
     * PENDING chưa đóng cọc.
     *
     * <p><b>Ví dụ:</b> {@code POST /api/payments/deposit/manual-confirm}</p>
     *
     * @param request bookingId + ghi chú đối soát
     * @return {@code 200 OK} với {@link DepositConfirmResponse}
     */
    @PostMapping("/deposit/manual-confirm")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<DepositConfirmResponse>> manualConfirmDeposit(
            @Valid @RequestBody ManualDepositConfirmRequest request) {

        DepositConfirmResponse result = sePayWebhookService.manualConfirmDeposit(request);

        return ResponseEntity.ok(
                ApiResponse.success("Xác nhận cọc thủ công thành công", result)
        );
    }

    /**
     * Chức năng: Xác nhận thanh toán mua gói thủ công khi webhook SePay lỗi —
     * admin/staff đối chiếu sao kê rồi bấm xác nhận. Chỉ áp dụng cho hóa đơn
     * subscription đang PENDING.
     *
     * <p><b>Ví dụ:</b> {@code POST /api/payments/subscription/manual-confirm}</p>
     *
     * @param request invoiceId + ghi chú đối soát
     * @return {@code 200 OK} với trạng thái hóa đơn sau xác nhận
     */
    @PostMapping("/subscription/manual-confirm")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<SubscriptionPaymentInitResponse>> manualConfirmSubscription(
            @Valid @RequestBody ManualSubscriptionConfirmRequest request) {

        SubscriptionPaymentInitResponse result = sePayWebhookService.manualConfirmSubscription(request);

        return ResponseEntity.ok(
                ApiResponse.success("Xác nhận thanh toán mua gói thủ công thành công", result)
        );
    }

    /**
     * Chức năng: Tra cứu lịch sử webhook SePay để admin đối soát — lọc theo
     * trạng thái xử lý (PROCESSED, ORPHAN, AMOUNT_MISMATCH, INVALID_BOOKING_STATE).
     *
     * <p><b>Ví dụ:</b> {@code GET /api/payments/webhook-logs?status=ORPHAN}</p>
     *
     * @param status trạng thái cần lọc, bỏ trống để lấy tất cả
     * @return {@code 200 OK} với danh sách {@link WebhookLogResponse}
     */
    @GetMapping("/webhook-logs")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<List<WebhookLogResponse>>> getWebhookLogs(
            @RequestParam(required = false) String status) {

        List<WebhookLogResponse> result = sePayWebhookService.getWebhookLogs(status);

        return ResponseEntity.ok(
                ApiResponse.success("Danh sách webhook log", result)
        );
    }
}
