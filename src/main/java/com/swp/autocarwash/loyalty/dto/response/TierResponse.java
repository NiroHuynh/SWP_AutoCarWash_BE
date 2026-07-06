package com.swp.autocarwash.loyalty.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mot dong trong bang tra cuu hang thanh vien (MEMBER/SILVER/GOLD/PLATINUM),
 * dung de UI hien thi man "So sanh hang thanh vien".
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TierResponse {

    private String tierName;

    /** Nguong diem toi thieu (accumulatedPoints) de dat hang nay. */
    private Integer minPoints;

    /** So ngay toi da duoc dat lich truoc doi voi hang nay. */
    private Integer bookingWindowDays;

    /** He so nhan diem khi tich diem. */
    private BigDecimal pointMultiple;

    /** Muc chi tieu muc tieu de giu hang nay (0 neu la hang thap nhat). */
    private BigDecimal retentionTargetAmount;

    /** Danh sach mo ta quyen loi cua hang nay. */
    private List<String> benefits;
}
