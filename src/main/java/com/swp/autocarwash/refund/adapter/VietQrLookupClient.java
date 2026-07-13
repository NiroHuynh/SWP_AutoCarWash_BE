package com.swp.autocarwash.refund.adapter;

import com.swp.autocarwash.refund.config.VietQrProperties;
import com.swp.autocarwash.refund.port.VietQrLookupPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

/**
 * Chức năng: Adapter gọi VietQR Lookup API thật để tra tên chủ tài khoản.
 *
 * <p>POST {url} body {@code {bin, accountNumber}} kèm header {@code x-client-id},
 * {@code x-api-key}. Mọi lỗi (không tồn tại / timeout / cấu hình thiếu) đều nuốt và
 * trả {@link Optional#empty()} để service quyết định fallback nhập tay (AC2c).</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Slf4j
@Component
@Profile("pro")
@RequiredArgsConstructor
public class VietQrLookupClient implements VietQrLookupPort {

    private final VietQrProperties properties;
    private final RestClient restClient = RestClient.create();

    @Override
    public Optional<String> lookupAccountName(String bin, String accountNumber) {
        if (isBlank(properties.getUrl()) || isBlank(properties.getClientId())
                || isBlank(properties.getApiKey())) {
            log.warn("VietQR lookup chưa được cấu hình (url/client-id/api-key) — bỏ qua lookup");
            return Optional.empty();
        }
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.post()
                    .uri(properties.getUrl())
                    .header("x-client-id", properties.getClientId())
                    .header("x-api-key", properties.getApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("bin", bin, "accountNumber", accountNumber))
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                return Optional.empty();
            }
            // VietQR trả { code: "00", desc, data: { accountName, accountNumber } }
            Object code = response.get("code");
            Object data = response.get("data");
            if ("00".equals(code) && data instanceof Map<?, ?> dataMap) {
                Object accountName = dataMap.get("accountName");
                if (accountName != null && !accountName.toString().isBlank()) {
                    return Optional.of(accountName.toString());
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            log.warn("VietQR lookup lỗi cho bin={} account={}: {}", bin, accountNumber, e.getMessage());
            return Optional.empty();
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
