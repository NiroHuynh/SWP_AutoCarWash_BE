package com.swp.autocarwash.refund.dto.response;

import lombok.*;

/**
 * Chức năng: Một lựa chọn ngân hàng cho dropdown chọn ngân hàng nhận hoàn tiền (AC2).
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankOptionResponse {

    private String bin;
    private String displayName;
    private String shortCode;
}
