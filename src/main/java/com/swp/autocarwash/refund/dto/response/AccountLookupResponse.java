package com.swp.autocarwash.refund.dto.response;

import lombok.*;

/**
 * Chức năng: Kết quả tra cứu tên chủ tài khoản (VietQR Lookup, AC2.1).
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountLookupResponse {

    private String bin;
    private String bankName;
    private String accountNumber;
    /** Tên chủ tài khoản trả về từ VietQR — dùng auto-fill refund_account_holder. */
    private String accountName;
}
