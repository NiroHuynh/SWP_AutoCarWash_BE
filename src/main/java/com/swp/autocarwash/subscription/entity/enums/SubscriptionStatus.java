package com.swp.autocarwash.subscription.entity.enums;

public enum SubscriptionStatus {
    ACTIVE,
    EXPIRED,
    CANCELED,
    // Gói vừa đăng ký, chờ khách thanh toán; chưa được hưởng quyền lợi rửa xe
    // (các query quyền lợi đều lọc ACTIVE). Chuyển ACTIVE khi thanh toán thành công.
    PENDING
}
