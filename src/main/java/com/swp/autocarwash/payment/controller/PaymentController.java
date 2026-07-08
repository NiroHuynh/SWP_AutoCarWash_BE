package com.swp.autocarwash.payment.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.payment.dto.request.CashPaymentRequest;
import com.swp.autocarwash.payment.dto.response.CashPaymentResponse;
import com.swp.autocarwash.payment.dto.response.InvoiceDetailResponse;
import com.swp.autocarwash.payment.service.PaymentService;
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
     * Chức năng: Staff/Admin xem chi tiết hóa đơn sau checkout (FE-63-US-01 AC02).
     *
     * <p><b>Ví dụ:</b> {@code GET /api/payments/invoices/16}</p>
     *
     * @param invoiceId id hóa đơn ({@code BookingInvoice.id})
     * @return {@code 200 OK} với {@link InvoiceDetailResponse}
     */
    @GetMapping("/invoices/{invoiceId}")
    @PreAuthorize("hasAnyAuthority('STAFF','ADMIN')")
    public ResponseEntity<ApiResponse<InvoiceDetailResponse>> getInvoiceDetail(@PathVariable Long invoiceId) {
        InvoiceDetailResponse data = paymentService.getInvoiceDetail(invoiceId);
        return ResponseEntity.ok(ApiResponse.success("Chi tiết hóa đơn", data));
    }
}
