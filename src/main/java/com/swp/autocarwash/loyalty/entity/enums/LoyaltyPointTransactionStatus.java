package com.swp.autocarwash.loyalty.entity.enums;

public enum LoyaltyPointTransactionStatus {
    EARN,        // Cộng điểm khi hoàn thành booking/mua hàng
    REDEEM,      // Dùng điểm để đổi ưu đãi
    ADJUST,      // Điều chỉnh điểm thủ công (admin)
    REFUND       // Hoàn lại điểm khi hủy giao dịch
}
