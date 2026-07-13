package com.swp.autocarwash.system.service.impl;


import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.system.entity.SystemSetting;
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

    /**
     * Số tiền cần chi để nhận 1 điểm.
     * Ví dụ:
     * 10000 => 10.000 VND = 1 Point
     */
    public static final String LOYALTY_EARN_RATE_VND_PER_POINT =
            "LOYALTY_EARN_RATE_VND_PER_POINT";

    /**
     * Giá trị quy đổi của 1 điểm.
     * Ví dụ:
     * 1000 => 1 Point = 1.000 VND
     */
    public static final String LOYALTY_REDEEM_RATE_VND_PER_POINT =
            "LOYALTY_REDEEM_RATE_VND_PER_POINT";

    /** Key duy nhat luu muc coc co dinh ap dung cho toan he thong (20.000d). */
    public static final String DEFAULT_DEPOSIT_AMOUNT = "DEFAULT_DEPOSIT_AMOUNT";
    /** Key cau hinh ngay reset diem loyalty thuong nien, dinh dang MM-DD (vd 01-01). */
    public static final String LOYALTY_RESET_MONTH_DAY = "LOYALTY_RESET_MONTH_DAY";
    /** Key cau hinh ty le VND tren 1 diem loyalty (vd 1000 = 1000d/diem). */
    public static final String LOYALTY_POINT_PER_VND = "LOYALTY_POINT_PER_VND";
    /** Key cau hinh so phut booking PENDING duoc cho chuyen khoan coc truoc khi tu dong huy. */
    public static final String PENDING_PAYMENT_TIMEOUT_MINUTES = "PENDING_PAYMENT_TIMEOUT_MINUTES";
    /** Key cau hinh prefix noi dung chuyen khoan hoan tien (vd RF => RF{refundId}). Dung o phase Admin. */
    public static final String REFUND_TRANSFER_CONTENT_PREFIX = "REFUND_TRANSFER_CONTENT_PREFIX";
    /** Key cau hinh template noi dung chuyen khoan QR hoan tien, thay the placeholder {booking_id}. */
    public static final String REFUND_TRANSFER_CONTENT_TEMPLATE = "REFUND_TRANSFER_CONTENT_TEMPLATE";
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
    public Integer getTransferLock(String settingKey) {
        String transferLock = systemSettingRepository.findBySettingKey(settingKey).orElseThrow(()
                -> new BusinessException(SYSTEM_SETTING_NOT_FOUND)).getSettingValue();
        try{
            return Integer.valueOf(transferLock);
        }catch(NumberFormatException e ){
            throw new BusinessException(INVALID_CONFIG_VALUE_FORMAT);
        }
    }

    @Override
    public Integer getMaxViolationLimit(String settingKey) {
        String violationLimit = systemSettingRepository.findBySettingKey(settingKey).orElseThrow(()
                -> new BusinessException(SYSTEM_SETTING_NOT_FOUND)).getSettingValue();
        try{
            return Integer.valueOf(violationLimit);
        }catch(NumberFormatException e ){
            throw new BusinessException(INVALID_CONFIG_VALUE_FORMAT);
        }
    }

    @Override
    public Integer getPendingPaymentTimeoutMinutes() {
        String timeout = systemSettingRepository.findBySettingKey(PENDING_PAYMENT_TIMEOUT_MINUTES).orElseThrow(()
                -> new BusinessException(SYSTEM_SETTING_NOT_FOUND)).getSettingValue();
        try{
            return Integer.valueOf(timeout);
        }catch(NumberFormatException e ){
            throw new BusinessException(INVALID_CONFIG_VALUE_FORMAT);
        }
    }

    public String getStringValue(String settingKey) {
        return systemSettingRepository.findBySettingKey(settingKey)
                .orElseThrow(() -> new BusinessException(SYSTEM_SETTING_NOT_FOUND))
                .getSettingValue();
    }


    @Override
    public BigDecimal getLoyaltyEarnRate() {
        return getIntegerValue(LOYALTY_EARN_RATE_VND_PER_POINT);
    }

    @Override
    public BigDecimal getLoyaltyRedeemRate() {
        return getIntegerValue(LOYALTY_REDEEM_RATE_VND_PER_POINT);
    }

    private BigDecimal getIntegerValue(String key) {
        SystemSetting setting = systemSettingRepository
                .findSystemSettingBySettingKeyAndIsActiveTrue(key)
                .orElseThrow(() -> new BusinessException(SYSTEM_SETTING_NOT_FOUND));
        return new BigDecimal(setting.getSettingValue());
    }
}
