package com.swp.autocarwash.loyalty.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Ket qua trang "Points History" khi khach filter theo nam (va thang).
 *
 * <p>Chuc nang: bao gom Total spending CUA CA NAM duoc chon (khong doi khi loc thang)
 * va danh sach giao dich diem khop filter nam+thang.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyHistoryResponse {

    /** Nam dang xem. */
    private Integer year;

    /** Thang dang loc (null neu xem ca nam). */
    private Integer month;

    /** Tong chi tieu (Booking + Subscription da thanh toan) cua CA NAM - khong doi khi loc thang. */
    private BigDecimal totalSpending;

    /** Danh sach giao dich diem khop filter, sap xep moi nhat len dau. */
    private List<LoyaltyPointTransactionResponse> transactions;
}
