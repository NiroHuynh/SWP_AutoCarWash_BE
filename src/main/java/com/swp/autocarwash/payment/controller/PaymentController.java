package com.swp.autocarwash.payment.controller;

import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.payment.dto.request.CashPaymentRequest;
import com.swp.autocarwash.payment.dto.request.ManualDepositConfirmRequest;
import com.swp.autocarwash.payment.dto.request.ManualSubscriptionConfirmRequest;
import com.swp.autocarwash.payment.dto.response.PaymentHistoryResponse;
import com.swp.autocarwash.payment.dto.response.PaymentTransactionHistoryResponse;
import com.swp.autocarwash.payment.dto.response.SubscriptionPaymentInitResponse;
import com.swp.autocarwash.payment.dto.response.CashPaymentResponse;
import com.swp.autocarwash.payment.dto.response.DepositConfirmResponse;
import com.swp.autocarwash.payment.dto.response.WebhookLogResponse;
import com.swp.autocarwash.payment.dto.response.InvoiceDetailResponse;
import com.swp.autocarwash.payment.dto.response.WebhookLogResponse;
import com.swp.autocarwash.payment.service.PaymentService;
import com.swp.autocarwash.payment.service.SePayWebhookService;
import com.swp.autocarwash.staff.util.StaffScopeResolver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final CustomerRepository customerRepository;
    private final StaffScopeResolver staffScopeResolver;

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
    @PreAuthorize("hasRole('STAFF')")
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
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
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
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
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
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<List<WebhookLogResponse>>> getWebhookLogs(
            @RequestParam(required = false) String status) {

        List<WebhookLogResponse> result = sePayWebhookService.getWebhookLogs(status);

        return ResponseEntity.ok(
                ApiResponse.success("Danh sách webhook log", result)
        );
    }

    /**
     * Chức năng: Admin/staff theo dõi & đối soát toàn bộ giao dịch thanh toán
     * thành công (FE-61C-US-02) — trả kèm KPI summary (tổng doanh thu + tổng
     * số giao dịch) tính trên đúng tập kết quả đang filter.
     *
     * <p><b>Ví dụ:</b> {@code GET /api/payments/transactions?method=CASH&bookingId=11}</p>
     * <p>Lọc theo chi nhánh: {@code GET /api/payments/transactions?type=DEPOSIT&stationId=1} —
     * giao dịch subscription không thuộc station nào nên khi truyền {@code stationId}
     * sẽ không xuất hiện; FE nên tách 2 tab (Subscription dùng {@code type=SUBSCRIPTION},
     * Booking dùng {@code type} khác kèm {@code stationId} tuỳ chọn).</p>
     *
     * @param method        lọc theo phương thức thanh toán, bỏ trống để lấy tất cả
     * @param status        lọc theo trạng thái giao dịch, bỏ trống để lấy tất cả
     * @param type          lọc theo loại giao dịch (DEPOSIT/FULL_PAYMENT/SUBSCRIPTION), bỏ trống để lấy tất cả
     * @param fromDate      lọc paidAt từ ngày này trở đi, bỏ trống = không giới hạn
     * @param toDate        lọc paidAt đến ngày này, bỏ trống = không giới hạn
     * @param bookingId     tra nhanh theo 1 Booking ID cụ thể, bỏ trống = không lọc
     * @param transactionId tra nhanh theo 1 Transaction ID cụ thể, bỏ trống = không lọc
     * @param stationId     lọc theo chi nhánh cụ thể, chỉ áp dụng cho giao dịch booking
     * @param communeId     lọc theo xã/phường, chỉ áp dụng cho giao dịch booking
     * @param provinceId    lọc theo tỉnh/thành, chỉ áp dụng cho giao dịch booking
     * @param phone         tìm gần đúng theo SĐT khách hàng (cả booking lẫn subscription), bỏ trống = không lọc
     * @return {@code 200 OK} với {@link PaymentTransactionHistoryResponse}
     */
    @GetMapping("/transactions")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<PaymentTransactionHistoryResponse>> getTransactionHistory(
            @AuthenticationPrincipal UserCustomerDetails principal,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(required = false) Long bookingId,
            @RequestParam(required = false) Long transactionId,
            @RequestParam(required = false) Integer stationId,
            @RequestParam(required = false) Integer communeId,
            @RequestParam(required = false) Integer provinceId,
            @RequestParam(required = false) String phone) {

        boolean isStaff = staffScopeResolver.isStaff(principal);
        Integer effStationId = isStaff ? staffScopeResolver.staffStationId(principal) : stationId;
        Integer effCommuneId = isStaff ? null : communeId;
        Integer effProvinceId = isStaff ? null : provinceId;

        PaymentTransactionHistoryResponse result = paymentService.getTransactionHistory(
                method, status, type, fromDate, toDate, bookingId, transactionId, effStationId, effCommuneId,
                effProvinceId, phone);

        return ResponseEntity.ok(
                ApiResponse.success("Lịch sử giao dịch thanh toán", result)
        );
    }

    /**
     * Chức năng: Khách hàng xem lịch sử giao dịch thanh toán thành công của
     * chính mình (FE-61C-US-01) — gồm cả cọc booking lẫn mua gói subscription.
     * Lọc thêm theo loại giao dịch và/hoặc khoảng ngày nếu có truyền.
     *
     * <p><b>Ví dụ:</b> {@code GET /api/payments/history?type=SUBSCRIPTION&fromDate=2026-01-01T00:00:00}</p>
     * <p>Hoặc lọc nhanh theo năm/tháng: {@code GET /api/payments/history?year=2026&month=7} —
     * nếu có {@code year}, nó sẽ override {@code fromDate}/{@code toDate} (tự tính khoảng
     * đầu-cuối tháng, hoặc cả năm nếu không kèm {@code month}).</p>
     *
     * @param principal khách hàng đang đăng nhập (suy ra customerId từ JWT)
     * @param type      lọc theo loại giao dịch, bỏ trống để lấy tất cả
     * @param fromDate  lọc paidAt từ ngày này trở đi, bỏ trống = không giới hạn (bị override nếu có {@code year})
     * @param toDate    lọc paidAt đến ngày này, bỏ trống = không giới hạn (bị override nếu có {@code year})
     * @param year      lọc nhanh theo năm, override fromDate/toDate nếu có truyền
     * @param month     lọc nhanh theo tháng (1-12), chỉ áp dụng khi có {@code year}; nếu bỏ trống thì lấy cả năm
     * @return {@code 200 OK} với danh sách {@link PaymentHistoryResponse}
     */
    @GetMapping("/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<PaymentHistoryResponse>>> getPaymentHistory(
            @AuthenticationPrincipal UserCustomerDetails principal,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        Long customerId = resolveCustomerId(principal);

        LocalDateTime effectiveFromDate = fromDate;
        LocalDateTime effectiveToDate = toDate;
        if (year != null) {
            if (month != null) {
                YearMonth ym = YearMonth.of(year, month);
                effectiveFromDate = ym.atDay(1).atStartOfDay();
                effectiveToDate = ym.atEndOfMonth().atTime(LocalTime.MAX);
            } else {
                effectiveFromDate = LocalDate.of(year, 1, 1).atStartOfDay();
                effectiveToDate = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);
            }
        }

        List<PaymentHistoryResponse> result =
                paymentService.getPaymentHistory(customerId, type, effectiveFromDate, effectiveToDate);

        return ResponseEntity.ok(
                ApiResponse.success("Lịch sử thanh toán", result)
        );
    }

    /** Suy ra customerId của khách đang đăng nhập từ token. */
    private Long resolveCustomerId(UserCustomerDetails principal) {
        Customer customer = customerRepository.findByUserId(principal.getUser().getId());
        if (customer == null) {
            throw new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        return customer.getId();
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
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ResponseEntity<ApiResponse<InvoiceDetailResponse>> getInvoiceDetail(
            @AuthenticationPrincipal UserCustomerDetails principal,
            @PathVariable Long invoiceId) {
        Integer staffStationId = staffScopeResolver.isStaff(principal)
                ? staffScopeResolver.staffStationId(principal)
                : null;
        InvoiceDetailResponse data = paymentService.getInvoiceDetail(invoiceId, staffStationId);
        return ResponseEntity.ok(ApiResponse.success("Chi tiết hóa đơn", data));
    }
}
