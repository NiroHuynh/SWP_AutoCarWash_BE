package com.swp.autocarwash.payment.entity.enums;

public enum SubscriptionInvoiceStatus {

    PENDING,      // Đã tạo hóa đơn, chờ thanh toán

    PAID,         // Đã thanh toán thành công

    CANCELLED,    // Hóa đơn đã bị hủy

    EXPIRED,      // Hóa đơn hết hạn thanh toán

    FAILED        // Thanh toán thất bại
}
