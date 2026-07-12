package com.swp.autocarwash.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Chức năng: Thông tin tài khoản ngân hàng nhận cọc/thanh toán đã liên kết với
 * SePay — dùng để build URL QR chuyển khoản trả về cho FE.
 *
 * @author Ngân
 * @version 1.0
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "sepay.bank")
public class SePayBankProperties {

    private String accountNumber;

    /** Mã ngân hàng theo chuẩn SePay, ví dụ "MBBank", "Vietcombank". */
    private String bankCode;

    private String accountName;
}
