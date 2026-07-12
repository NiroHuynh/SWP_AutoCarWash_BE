package com.swp.autocarwash.subscription.service;

import com.swp.autocarwash.payment.dto.response.SubscriptionPaymentInitResponse;
import com.swp.autocarwash.subscription.dto.request.RegisterUnlimitedSubscriptionRequest;

/**
 * Chức năng: Luồng mua/gia hạn gói Unlimited CÓ thanh toán (FE-US-56-04).
 *
 * <p>Tách riêng khỏi {@code UnlimitSubscriptionService} của Phong để không đụng
 * code hiện có: service này chỉ <b>gọi</b> các method đọc/save sẵn có của Phong,
 * tạo gói ở trạng thái PENDING và khởi tạo hóa đơn QR; gói chỉ ACTIVE sau khi
 * thanh toán thành công (do {@code SubscriptionRenewalListener} xử lý).</p>
 *
 * @author Ngân
 * @version 1.0
 */
public interface SubscriptionPurchaseService {

    /**
     * Đăng ký gói Unlimited mới: tạo gói PENDING + hóa đơn PENDING, trả về thông
     * tin QR để khách thanh toán.
     */
    SubscriptionPaymentInitResponse registerUnlimited(RegisterUnlimitedSubscriptionRequest request);

    /**
     * Gia hạn gói Unlimited đang có (ACTIVE/EXPIRED): tạo hóa đơn gia hạn PENDING,
     * trả về QR. endDate được cộng dồn khi thanh toán thành công.
     */
    SubscriptionPaymentInitResponse renewUnlimited(Long subscriptionId);
}
