package com.swp.autocarwash.loyalty.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Mot dong giao dich diem trong "Points History".
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyPointTransactionResponse {

    private Long id;

    /** Loai giao dich: EARN / REDEEM / RESET. */
    private String transactionType;

    /** So diem thay doi (duong = cong, am = tru). */
    private Integer points;

    /** So du diem sau giao dich (snapshot). */
    private Integer balanceAfter;

    /** Booking lien quan (null neu giao dich khong gan booking). */
    private Long bookingId;

    /** Thoi diem check-out (hoan tat) cua booking lien quan; null neu booking chua check-out hoac khong gan booking. */
    private LocalDateTime bookingCheckOutAt;

    /** Ten goi dich vu cua booking lien quan (null neu giao dich khong gan booking). */
    private String servicePackageName;

    /** Nguon phat sinh giao dich: BOOKING / SUBSCRIPTION / MANUAL / ADJUSTMENT / DEPOSIT_REFUND. */
    private String description;

    private Instant createdAt;
}
