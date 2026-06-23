package com.swp.autocarwash.system.adapter;

import com.swp.autocarwash.booking.port.SystemSettingPort;
import com.swp.autocarwash.system.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SystemSettingBookingAdapter implements SystemSettingPort {

    private final SystemSettingService systemSettingService;

    @Override
    public BigDecimal getDepositAmount(String settingKey) {
        BigDecimal depositAmount = systemSettingService.getDepositAmount(settingKey);
        return depositAmount ;
    }
}
