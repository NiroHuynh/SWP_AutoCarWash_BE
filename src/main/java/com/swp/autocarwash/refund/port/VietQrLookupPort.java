package com.swp.autocarwash.refund.port;

import java.util.Optional;

/**
 * Chức năng: Cổng tra cứu tên chủ tài khoản ngân hàng qua VietQR Lookup.
 *
 * @author KimNgan
 * @version 1.0
 */
public interface VietQrLookupPort {

    /**
     * Tra cứu tên chủ tài khoản.
     *
     * @param bin           mã napas BIN của ngân hàng
     * @param accountNumber số tài khoản
     * @return Optional chứa tên chủ tài khoản; rỗng nếu không tra được (không tồn tại / lỗi / timeout)
     */
    Optional<String> lookupAccountName(String bin, String accountNumber);
}
