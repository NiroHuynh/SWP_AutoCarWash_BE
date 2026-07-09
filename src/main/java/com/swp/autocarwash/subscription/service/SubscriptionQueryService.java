package com.swp.autocarwash.subscription.service;

import com.swp.autocarwash.subscription.dto.response.ActiveSubscriptionResponse;

/**
 * Chức năng: Truy vấn thông tin subscription phơi ra cho khách hàng (FE) —
 * tách riêng khỏi {@link UnlimitSubscriptionService}/{@link FamilySubscriptionService}
 * vì 2 service đó chỉ phục vụ logic nội bộ khi tạo booking.
 *
 * @author Ngân
 * @version 1.0
 */
public interface SubscriptionQueryService {

    /**
     * Chức năng: Lấy gói subscription đang hoạt động (ACTIVE, còn hạn) của
     * khách hàng — ưu tiên tìm ở UnlimitSubscription trước, nếu không có thì
     * tìm ở FamilySubscription. Tại 1 thời điểm khách chỉ có đúng 1 gói active.
     *
     * @param customerId id khách hàng (suy ra từ JWT)
     * @return thông tin gói active, hoặc {@code null} nếu khách không có gói nào active
     */
    ActiveSubscriptionResponse getActiveSubscription(Long customerId);
}
