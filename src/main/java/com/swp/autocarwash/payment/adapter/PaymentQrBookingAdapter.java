package com.swp.autocarwash.payment.adapter;

import com.swp.autocarwash.booking.port.PaymentQrPort;
import com.swp.autocarwash.payment.util.SePayQrBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Chức năng: Adapter triển khai {@link PaymentQrPort} — cầu nối giữa booking
 * module và payment module để dựng URL ảnh QR chuyển khoản.
 *
 * @author Ngân
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class PaymentQrBookingAdapter implements PaymentQrPort {

    private final SePayQrBuilder sePayQrBuilder;

    @Override
    public String buildQrImageUrl(BigDecimal amount, String content) {
        return sePayQrBuilder.buildQrImageUrl(amount, content);
    }
}
