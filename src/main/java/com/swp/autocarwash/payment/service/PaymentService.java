package com.swp.autocarwash.payment.service;

import com.swp.autocarwash.payment.dto.request.CashPaymentRequest;
import com.swp.autocarwash.payment.dto.response.CashPaymentResponse;
import com.swp.autocarwash.payment.dto.response.PaymentHistoryResponse;
import com.swp.autocarwash.payment.dto.response.PaymentTransactionHistoryResponse;

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

    /**
     * Chức năng: Toàn bộ giao dịch thanh toán thành công cho admin đối soát
     * (FE-61C-US-02) — không giới hạn theo customer, kèm KPI summary (tổng
     * doanh thu + tổng số giao dịch) tính trên đúng tập kết quả đang filter.
     *
     * @param method        lọc theo phương thức thanh toán, null = lấy hết
     * @param status        lọc theo trạng thái giao dịch, null = lấy hết
     * @param type          lọc theo loại giao dịch (DEPOSIT/FULL_PAYMENT/SUBSCRIPTION), null = lấy hết
     * @param fromDate      lọc paidAt từ ngày này trở đi, null = không giới hạn
     * @param toDate        lọc paidAt đến ngày này, null = không giới hạn
     * @param bookingId     lọc đúng 1 booking, null = không lọc
     * @param transactionId lọc đúng 1 giao dịch, null = không lọc
     * @param stationId     lọc theo chi nhánh — chỉ áp dụng cho giao dịch booking,
     *                      giao dịch subscription không thuộc station nào nên sẽ
     *                      bị loại khỏi kết quả nếu truyền param này
     * @param phone         tìm gần đúng theo SĐT khách hàng (booking lẫn subscription), null = không lọc
     * @return summary + danh sách giao dịch, mới nhất trước
     */
    PaymentTransactionHistoryResponse getTransactionHistory(
            String method, String status, String type, LocalDateTime fromDate, LocalDateTime toDate,
            Long bookingId, Long transactionId, Integer stationId, String phone);
}
