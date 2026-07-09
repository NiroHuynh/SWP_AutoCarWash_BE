package com.swp.autocarwash.payment.service;

import com.swp.autocarwash.payment.dto.request.SubscriptionPaymentInitRequest;
import com.swp.autocarwash.payment.dto.response.SubscriptionPaymentInitResponse;

/**
 * Chức năng: Contract cho module subscription gọi sang khi khách mua gói —
 * payment tạo hóa đơn PENDING và trả thông tin chuyển khoản; việc xác nhận
 * tiền về do webhook SePay / manual confirm đảm nhiệm, kết quả bắn ra qua
 * {@link com.swp.autocarwash.payment.event.SubscriptionInvoicePaidEvent}.
 *
 * @author Ngân
 * @version 1.0
 */
public interface SubscriptionPaymentService {

    /**
     * Chức năng: Khởi tạo thanh toán mua gói — tạo {@code SubscriptionInvoice}
     * status PENDING gắn customer + bản ghi gói đang chờ, trả về nội dung
     * chuyển khoản "SUB{invoiceId}" + số tiền cho FE hiển thị QR.
     *
     * @param request customerId, planPrice, id gói unlimited/family đang chờ
     * @return invoiceId + transferContent + amount
     */
    SubscriptionPaymentInitResponse initiatePayment(SubscriptionPaymentInitRequest request);

    /**
     * Chức năng: Đọc trạng thái + thông tin thanh toán của một hóa đơn mua gói
     * (AC02/AC03 — FE poll để chuyển UI khi PENDING/PAID/FAILED). Kiểm tra hóa đơn
     * thuộc đúng customer đang đăng nhập; {@code qrImageUrl} chỉ trả khi còn PENDING.
     *
     * @param invoiceId  id hóa đơn cần tra
     * @param customerId id khách đang đăng nhập (verify quyền sở hữu)
     */
    SubscriptionPaymentInitResponse getInvoiceStatus(Long invoiceId, Long customerId);
}
