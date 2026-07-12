package com.swp.autocarwash.loyalty.dto.response;

import com.swp.autocarwash.loyalty.entity.enums.TierChangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Mot dong lich su chuyen hang thanh vien trong "Tier History".
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TierHistoryResponse {

    private Long id;

    /** Hang truoc do; null neu day la lan gan hang dau tien. */
    private String oldTierName;

    private String newTierName;

    /** Snapshot tai thoi diem chuyen hang: diem tich luy (checkout) hoac tong chi tieu VND (annual job). */
    private Integer valueAtTransition;

    private TierChangeType changeType;

    /** Booking gay ra lan chuyen hang nay (null neu khong xac dinh duoc). */
    private Long bookingId;

    private Instant createdAt;
}
