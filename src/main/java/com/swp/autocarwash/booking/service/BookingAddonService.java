package com.swp.autocarwash.booking.service;

import java.util.List;

public interface BookingAddonService {

    /**
     * Attach addon service vào booking.
     *
     * Flow:
     * - Kiểm tra addon tồn tại.
     * - Lấy giá hiện tại của addon.
     * - Tạo booking_addon snapshot giá.
     *
     * @param bookingId booking id
     * @param addonIds danh sách addon được chọn
     *
     * @Author Phong
     */
    void attachAddon(
            Long bookingId,
            List<Integer> addonIds
    );

}
