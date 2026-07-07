package com.swp.autocarwash.payment.controller;

import com.swp.autocarwash.payment.dto.request.SePayWebhookRequest;
import com.swp.autocarwash.payment.service.SePayWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Chức năng: Nhận webhook giao dịch ngân hàng từ SePay để xác nhận tiền cọc
 * booking. Endpoint permitAll trong SecurityConfig, xác thực bằng API Key
 * trong header {@code Authorization: Apikey <key>} theo chuẩn SePay.
 *
 * <p>Base URL: {@code /api/webhooks/sepay}</p>
 *
 * @author Ngân
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/webhooks/sepay")
@RequiredArgsConstructor
public class SePayWebhookController {

    private final SePayWebhookService sePayWebhookService;

    @Value("${sepay.webhook.api-key}")
    private String webhookApiKey;

    /**
     * Chức năng: Xử lý một giao dịch chuyển khoản SePay báo về. Luôn trả 200
     * sau khi đã ghi nhận (kể cả orphan/sai tiền — đã được log lại) để SePay
     * không retry; chỉ trả 401 khi sai API key.
     *
     * @param authorization header dạng "Apikey xxx"
     * @param request payload giao dịch từ SePay
     * @return {@code 200 {"success": true}}
     */
    @PostMapping
    public ResponseEntity<Map<String, Boolean>> receiveWebhook(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody SePayWebhookRequest request) {

        if (!isValidApiKey(authorization)) {
            log.warn("SePay webhook bị từ chối: API key không hợp lệ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false));
        }

        sePayWebhookService.handleWebhook(request);

        return ResponseEntity.ok(Map.of("success", true));
    }

    private boolean isValidApiKey(String authorization) {
        return authorization != null
                && authorization.trim().equalsIgnoreCase("Apikey " + webhookApiKey);
    }
}
