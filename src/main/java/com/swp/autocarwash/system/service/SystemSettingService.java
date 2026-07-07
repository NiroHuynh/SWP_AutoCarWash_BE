package com.swp.autocarwash.system.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface SystemSettingService {
    BigDecimal getDepositAmount(String settingKey);
    Integer getTransferLock(String settingKey);

    /**
     * Doc gia tri String tho cua 1 setting theo key.
     * Nem BusinessException(SYSTEM_SETTING_NOT_FOUND) neu khong tim thay key.
     */
    String getStringValue(String settingKey);

    BigDecimal getLoyaltyEarnRate();

    BigDecimal getLoyaltyRedeemRate();

    Integer getMaxViolationLimit(String settingKey);
}
