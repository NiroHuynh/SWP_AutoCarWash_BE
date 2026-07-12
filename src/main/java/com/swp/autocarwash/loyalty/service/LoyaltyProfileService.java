package com.swp.autocarwash.loyalty.service;

import com.swp.autocarwash.loyalty.dto.response.LoyaltyHistoryResponse;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyProfileResponse;
import com.swp.autocarwash.loyalty.dto.response.TierHistoryResponse;
import com.swp.autocarwash.loyalty.dto.response.TierResponse;
import com.swp.autocarwash.loyalty.entity.CustomerTier;

import java.util.List;

/**
 * Nghiep vu xem diem loyalty, ngay reset, chi tieu va lich su diem (FE-42-US-01).
 *
 * @author KimNgan
 * @version 1.0
 */
public interface LoyaltyProfileService {

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

    /**
     * Ghi nhan 1 lan chuyen hang neu oldTier/newTier khac nhau, nhan thang 2 tier thay vi
     * tu suy tu diem - dung khi hang cu can lay tu FK thuc te (vd danh gia thuong nien,
     * noi accumulatedPoints co the da bi reset ve 0 va khong con phan anh dung hang truoc do).
     *
     * @param oldTier          hang truoc khi danh gia (null = khach chua tung co hang, khong ghi)
     * @param newTier          hang sau khi danh gia (null = khong xac dinh duoc, khong ghi)
     * @param valueAtTransition snapshot tai thoi diem chuyen hang, luu vao lich su de tham khao -
     *                          diem tich luy (flow checkout) hoac tong tien chi tieu VND (annual job)
     * @param bookingId        booking gay ra lan chuyen hang nay, null neu khong xac dinh duoc
     */
    void recordTierChangeIfDifferent(Long customerId, CustomerTier oldTier, CustomerTier newTier,
                                      int valueAtTransition, Long bookingId);
}
