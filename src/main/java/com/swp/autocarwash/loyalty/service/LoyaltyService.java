package com.swp.autocarwash.loyalty.service;

import com.swp.autocarwash.loyalty.dto.response.LoyaltyHistoryResponse;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyProfileResponse;
import com.swp.autocarwash.loyalty.dto.response.TierHistoryResponse;
import com.swp.autocarwash.loyalty.dto.response.TierResponse;

import java.util.List;

/**
 * Nghiep vu xem diem loyalty, ngay reset, chi tieu va lich su diem (FE-42-US-01).
 *
 * @author KimNgan
 * @version 1.0
 */
public interface LoyaltyService {

    /** Du lieu trang Loyalty Profile cua khach dang dang nhap. */
    LoyaltyProfileResponse getLoyaltyProfile(Long customerId);

    /**
     * Lich su diem + tong chi tieu ca nam, filter theo nam (va thang tuy chon).
     *
     * @param year  nam can xem, null = nam hien tai
     * @param month thang can loc (1-12), null = ca nam
     */
    LoyaltyHistoryResponse getPointHistory(Long customerId, Integer year, Integer month);

    /** Bang tra cuu tat ca hang thanh vien kem nguong diem va quyen loi, sap theo minPoints tang dan. */
    List<TierResponse> getTierList();

    /**
     * Lich su chuyen hang thanh vien cua khach, moi nhat len dau, filter theo nam (va thang tuy chon).
     *
     * @param year  nam can xem, null = nam hien tai
     * @param month thang can loc (1-12), null = ca nam
     */
    List<TierHistoryResponse> getTierHistory(Long customerId, Integer year, Integer month);

    /**
     * Ghi nhan 1 lan chuyen hang neu accumulatedPoints truoc/sau khac hang.
     * Chua co noi nao goi ham nay - viec cong diem se goi trong task tuong lai;
     * ham nay duoc public de future feature (vd payment/booking) goi qua bean LoyaltyService.
     *
     * @param previousAccumulatedPoints diem tich luy TRUOC khi cong/tru
     * @param newAccumulatedPoints      diem tich luy SAU khi cong/tru
     * @param bookingId                 booking gay ra lan cong diem nay, null neu khong xac dinh duoc
     */
    void recordTierTransitionIfChanged(Long customerId, int previousAccumulatedPoints,
                                        int newAccumulatedPoints, Long bookingId);
}
