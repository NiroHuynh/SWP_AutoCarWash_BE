package com.swp.autocarwash.refund.adapter.mock;

import com.swp.autocarwash.refund.port.VietQrLookupPort;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Chức năng: Mock VietQR Lookup cho profile không phải "pro" (test).
 * Trả tên giả lập cố định để không phụ thuộc mạng ngoài.
 *
 * @author KimNgan
 * @version 1.0
 */
@Component
@Profile("!pro")
public class MockVietQrLookupAdapter implements VietQrLookupPort {

    @Override
    public Optional<String> lookupAccountName(String bin, String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            return Optional.empty();
        }
        return Optional.of("NGUYEN VAN A");
    }
}
