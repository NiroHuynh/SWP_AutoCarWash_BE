package com.swp.autocarwash.payment.service;

import com.swp.autocarwash.payment.dto.request.CashPaymentRequest;
import com.swp.autocarwash.payment.dto.response.CashPaymentResponse;
import com.swp.autocarwash.payment.dto.response.PaymentHistoryResponse;

import java.time.LocalDateTime;
import java.util.List;

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
     * Chức năng: Lịch sử giao dịch thanh toán thành công của khách hàng
     * (FE-61C-US-01) — gồm cả cọc booking lẫn mua gói subscription, lọc thêm
     * theo loại giao dịch và/hoặc khoảng ngày nếu có truyền.
     *
     * @param customerId id khách hàng (suy ra từ JWT, không tin client)
     * @param type       lọc theo loại giao dịch (DEPOSIT/FULL_PAYMENT/SUBSCRIPTION), null = lấy hết
     * @param fromDate   lọc paidAt từ ngày này trở đi, null = không giới hạn
     * @param toDate     lọc paidAt đến ngày này, null = không giới hạn
     * @return danh sách giao dịch, mới nhất trước
     */
    List<PaymentHistoryResponse> getPaymentHistory(
            Long customerId, String type, LocalDateTime fromDate, LocalDateTime toDate);
}
