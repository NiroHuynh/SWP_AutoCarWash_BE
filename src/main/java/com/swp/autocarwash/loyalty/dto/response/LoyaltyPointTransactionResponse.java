package com.swp.autocarwash.loyalty.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

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

    /** Ngay hen cua booking lien quan (null neu giao dich khong gan booking). */
    private LocalDate bookingDate;

    /** Ten goi dich vu cua booking lien quan (null neu giao dich khong gan booking). */
    private String servicePackageName;

    private Instant createdAt;
}
