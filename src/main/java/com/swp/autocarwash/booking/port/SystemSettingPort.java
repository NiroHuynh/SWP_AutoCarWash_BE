package com.swp.autocarwash.booking.port;

import java.math.BigDecimal;

public interface SystemSettingPort {
    BigDecimal getDepositAmount(String settingKey);

    /** So phut booking PENDING duoc cho chuyen khoan coc truoc khi tu dong huy. */
    Integer getPendingPaymentTimeoutMinutes();
}
