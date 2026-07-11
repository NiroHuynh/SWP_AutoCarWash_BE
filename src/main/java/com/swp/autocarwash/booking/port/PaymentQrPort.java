package com.swp.autocarwash.booking.port;

import java.math.BigDecimal;

/**
 * Chức năng: Cầu nối sang module payment để dựng URL ảnh QR chuyển khoản
 * (VietQR qua SePay) cho màn hình thanh toán cọc booking.
 */
public interface PaymentQrPort {

    /**
     * @param amount  số tiền cần chuyển
     * @param content nội dung chuyển khoản, ví dụ "BK123"
     * @return URL ảnh QR đã điền sẵn tài khoản/số tiền/nội dung
     */
    String buildQrImageUrl(BigDecimal amount, String content);
}
