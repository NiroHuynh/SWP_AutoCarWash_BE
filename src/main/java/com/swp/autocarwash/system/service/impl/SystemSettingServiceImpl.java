package com.swp.autocarwash.system.service.impl;


import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.system.repository.SystemSettingRepository;
import com.swp.autocarwash.system.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.swp.autocarwash.common.exception.code.ErrorCode.INVALID_CONFIG_VALUE_FORMAT;
import static com.swp.autocarwash.common.exception.code.ErrorCode.SYSTEM_SETTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SystemSettingServiceImpl implements SystemSettingService {

    /** Key duy nhat luu muc coc co dinh ap dung cho toan he thong (20.000d). */
    public static final String DEFAULT_DEPOSIT_AMOUNT = "DEFAULT_DEPOSIT_AMOUNT";
    /** Key cau hinh ngay reset diem loyalty thuong nien, dinh dang MM-DD (vd 01-01). */
    public static final String LOYALTY_RESET_MONTH_DAY = "LOYALTY_RESET_MONTH_DAY";
    /** Key cau hinh ty le VND tren 1 diem loyalty (vd 1000 = 1000d/diem). */
    public static final String LOYALTY_POINT_PER_VND = "LOYALTY_POINT_PER_VND";
    private final SystemSettingRepository systemSettingRepository;

    /**
     * Doc gia tri cua 1 setting va parse sang BigDecimal.
     * Neu khong tim thay key trong DB, hoac gia tri luu khong parse duoc thanh so,
     * nem BusinessException ngay - KHONG dung gia tri mac dinh ngam trong code,
     * de tranh truong hop Admin quen cau hinh ma he thong van chay voi so sai.
     */
    @Override
    public BigDecimal getDepositAmount(String settingKey) {
        String depositAmount = systemSettingRepository.findBySettingKey(settingKey).orElseThrow(()
                -> new BusinessException(SYSTEM_SETTING_NOT_FOUND)).getSettingValue();
        try{
            return new BigDecimal(depositAmount);
        }catch(NumberFormatException e ){
            throw new BusinessException(INVALID_CONFIG_VALUE_FORMAT);
        }
    }

    @Override
    public String getStringValue(String settingKey) {
        return systemSettingRepository.findBySettingKey(settingKey)
                .orElseThrow(() -> new BusinessException(SYSTEM_SETTING_NOT_FOUND))
                .getSettingValue();
    }
}
