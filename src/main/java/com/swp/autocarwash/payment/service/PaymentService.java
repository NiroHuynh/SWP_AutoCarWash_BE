package com.swp.autocarwash.payment.service;

import com.swp.autocarwash.payment.dto.request.CashPaymentRequest;
import com.swp.autocarwash.payment.dto.response.CashPaymentResponse;
import com.swp.autocarwash.payment.dto.response.InvoiceDetailResponse;

/**
 * Chức năng: Nghiệp vụ xử lý thanh toán tại quầy.
 *
 * @author Ngọc
 * @version 1.0
 */
public interface PaymentService {

    /**
     * Chức năng: Staff thu tiền mặt tại quầy cho một booking đã COMPLETED.
     * Tính số tiền còn phải thu (tổng tiền - tiền cọc đã trả nếu có), tạo/cập
     * nhật hoá đơn ({@code BookingInvoice}) và giao dịch thanh toán
     * ({@code Payment}), sau đó chuyển booking sang CHECK_OUT.
     *
     * @param request bookingId + receivedAmount (số tiền khách đưa)
     * @return chi tiết giao dịch: tổng tiền cần thu, tiền thừa, status mới
     */
    CashPaymentResponse processCashPayment(CashPaymentRequest request);

    /**
     * Chức năng: Staff xem chi tiết hóa đơn sau khi checkout (FE-63-US-01 AC02).
     *
     * @param invoiceId id hóa đơn ({@code BookingInvoice.id})
     * @return chi tiết đầy đủ: booking info, danh sách dịch vụ, giảm giá, số tiền thực trả, phương thức thanh toán
     */
    InvoiceDetailResponse getInvoiceDetail(Long invoiceId);
}
