package com.swp.autocarwash.payment.util;

import com.swp.autocarwash.payment.config.SePayBankProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

/**
 * Chức năng: Dựng URL ảnh QR VietQR qua SePay (https://qr.sepay.vn/img) từ tài
 * khoản ngân hàng cấu hình sẵn — dùng chung cho thanh toán cọc booking và mua
 * gói subscription, để FE chỉ cần {@code <img src="...">} mà không cần tự
 * build chuỗi QR.
 *
 * @author Ngân
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class SePayQrBuilder {

    private static final String QR_BASE_URL = "https://qr.sepay.vn/img";

    private final SePayBankProperties bankProperties;

    /**
     * @param amount  số tiền cần chuyển
     * @param content nội dung chuyển khoản, ví dụ "BK123" hoặc "SUB45"
     * @return URL ảnh QR đã điền sẵn số tài khoản, ngân hàng, số tiền, nội dung
     */
    public String buildQrImageUrl(BigDecimal amount, String content) {
        return UriComponentsBuilder.fromUriString(QR_BASE_URL)
                .queryParam("acc", bankProperties.getAccountNumber())
                .queryParam("bank", bankProperties.getBankCode())
                .queryParam("amount", amount.toBigInteger())
                .queryParam("des", content)
                .toUriString();
    }
}
