package com.swp.autocarwash.system.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface SystemSettingService {
    BigDecimal getDepositAmount(String settingKey);

    BigDecimal getLoyaltyEarnRate();

    BigDecimal getLoyaltyRedeemRate();
}
