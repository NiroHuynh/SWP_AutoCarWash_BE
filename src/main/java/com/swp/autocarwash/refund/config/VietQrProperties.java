package com.swp.autocarwash.refund.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Chức năng: Cấu hình VietQR Lookup API (tra cứu tên chủ tài khoản từ BIN + số TK).
 *
 * <p>Đăng ký tại vietqr.io để lấy {@code clientId}/{@code apiKey}.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "vietqr.lookup")
public class VietQrProperties {

    /** Endpoint lookup, mặc định https://api.vietqr.io/v2/lookup */
    private String url;
    private String clientId;
    private String apiKey;
}
