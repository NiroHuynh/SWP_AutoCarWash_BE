package com.swp.autocarwash.loyalty.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Du lieu tong quan trang Loyalty Profile cua khach hang.
 *
 * <p>Chuc nang: gom so du diem, ngay reset thuong nien + countdown (AC01),
 * tien do len hang tiep theo, tong chi tieu nam hien tai (year-to-date) va
 * tien do giu hang (retention).</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyProfileResponse {

    /** Diem kha dung hien tai (bi dua ve 0 sau ngay reset thuong nien - AC02). */
    private Integer totalPoints;

    /** Diem cong don lifetime, dung de xet hang thanh vien. */
    private Integer accumulatedPoints;

    /** Ngay reset diem thuong nien sap toi (AC01). */
    private LocalDate upcomingResetDate;

    /** So ngay con lai toi ngay reset (countdown). */
    private long daysUntilReset;

    /** Hang thanh vien hien tai. */
    private String tierName;

    /** Hang thanh vien ke tiep (null neu da o hang cao nhat). */
    private String nextTierName;

    /** So diem con thieu de len hang ke tiep (null neu da o hang cao nhat). */
    private Integer pointsToNextTier;

    /** So tien (VND) con thieu de len hang ke tiep (null neu da o hang cao nhat). */
    private BigDecimal amountToNextTier;

    /** Tong chi tieu (Booking + Subscription da thanh toan) tu 1/1 nam nay den bay gio. */
    private BigDecimal currentTotalSpending;

    /**
     * Muc chi tieu muc tieu de giu hang hien tai, lay tu cau hinh cua tier
     * (customer_tier.retention_target_amount). Null neu khach chua co tier.
     */
    private BigDecimal retentionTargetAmount;

    /**
     * Muc chi tieu da tich luy trong chu ky giu hang hien tai (Booking + Subscription
     * da thanh toan), tinh tu ngay reset diem GAN NHAT den bay gio.
     */
    private BigDecimal retentionCurrentAmount;

    /**
     * Ngay ket thuc chu ky giu hang hien tai - chinh la ngay reset diem KE TIEP
     * (chu ky giu hang = 1 nam, bat dau dung vao ngay reset diem).
     */
    private LocalDate retentionEndDate;

    /** Trang thai chu ky giu hang hien tai (luon "IN_PROGRESS" vi day la chu ky dang chay). */
    private String retentionStatus;
}
