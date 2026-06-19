package com.swp.autocarwash.booking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Thông tin một dịch vụ bổ sung (addon) trong booking chi tiết.
 *
 * @author KimNgan
 * @version 1.0
 */
@Getter
@Builder
public class AddonInfo {

    /** Tên dịch vụ bổ sung (ví dụ: "Vacuum", "Polish"). */
    private String addonName;

    /** Giá áp dụng thực tế của addon trong booking này. */
    private BigDecimal addonPrice;
}
