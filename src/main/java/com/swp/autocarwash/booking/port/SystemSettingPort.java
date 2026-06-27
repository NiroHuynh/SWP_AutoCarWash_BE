package com.swp.autocarwash.booking.port;

import java.math.BigDecimal;

public interface SystemSettingPort {
    BigDecimal getDepositAmount(String settingKey);
}
