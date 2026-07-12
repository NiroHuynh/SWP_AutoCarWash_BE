package com.swp.autocarwash.system.service.impl;


import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.system.dto.request.CreateSettingRequest;
import com.swp.autocarwash.system.dto.request.UpdateSettingRequest;
import com.swp.autocarwash.system.dto.response.SystemSettingResponse;
import com.swp.autocarwash.system.entity.SystemSetting;
import com.swp.autocarwash.system.repository.SystemSettingRepository;
import com.swp.autocarwash.system.service.SystemSettingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.swp.autocarwash.common.exception.code.ErrorCode.INVALID_CONFIG_VALUE_FORMAT;
import static com.swp.autocarwash.common.exception.code.ErrorCode.SYSTEM_SETTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SystemSettingServiceImpl implements SystemSettingService {

    private final SystemSettingRepository settingRepository;
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

    // 1. LẤY DANH SÁCH GOM NHÓM THEO CATEGORY (Dùng vòng lặp for)
    @Override
    @Transactional
    public Map<String, List<SystemSettingResponse>> getAllSettingsGrouped() {

            List<SystemSetting> allSettings = settingRepository.findAll();
            Map<String, List<SystemSettingResponse>> groupedMap = new HashMap<>();

            for (SystemSetting setting : allSettings) {
                String category = setting.getCategory();

                SystemSettingResponse responseDTO = SystemSettingResponse.builder()
                        .id(setting.getId())
                        .settingKey(setting.getSettingKey())
                        .settingValue(setting.getSettingValue())
                        .category(category)
                        .dataType(setting.getDataType())
                        .description(setting.getDescription())
                        .build();

                if (!groupedMap.containsKey(category)) {
                    groupedMap.put(category, new ArrayList<>());
                }

                groupedMap.get(category).add(responseDTO);
            }

            return groupedMap;
    }

    // 2. TẠO MỚI CẤU HÌNH (CREATE)
    @Override
    @Transactional
    public SystemSettingResponse createSetting(CreateSettingRequest request) {
        if (settingRepository.existsBySettingKey(request.getSettingKey())) {
            throw new BusinessException(ErrorCode.DUPLICATE_SETTING_KEY);
        }

        String finalCategory = request.getCategory() != null ? request.getCategory().trim() : "System";
        if (finalCategory.isEmpty()) {
            finalCategory = "System"; // Mặc định nếu Admin để trống nhóm
        }

        SystemSetting newSetting = SystemSetting.builder()
                .settingKey(request.getSettingKey().toUpperCase())
                .settingValue(request.getSettingValue().trim())
                .category(finalCategory)
                .dataType(request.getDataType().toUpperCase())
                .description(request.getDescription())
                .build();

        SystemSetting saved = settingRepository.save(newSetting);
        return convertToResponseDTO(saved);
    }

    // 3. CHỈNH SỬA CẤU HÌNH (UPDATE - Chỉ ghi đè value)
    @Override
    @Transactional
    public SystemSettingResponse updateSetting(Long id, UpdateSettingRequest request) {
        // 1. Kiểm tra sự tồn tại của cấu hình trong Database
        SystemSetting setting = settingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.SYSTEM_SETTING_NOT_FOUND));

        // 2. BỘ LỌC KIỂM DUYỆT ĐỘNG (DYNAMIC VALIDATION) - Dựa trên kiểu dữ liệu của cấu hình
        String newValue = request.getSettingValue();

        if (newValue != null) {
            String dataType = setting.getDataType();

            // Trường hợp A: Cấu hình yêu cầu kiểu dữ liệu là Số (NUMBER)
            if ("NUMBER".equalsIgnoreCase(dataType)) {
                try {
                    int numberValue = Integer.parseInt(newValue);

                    // Chốt chặn 1: Tất cả các số vận hành hệ thống (ngày, tiền, điểm...) không được phép âm
                    if (numberValue < 0) {
                        throw new BusinessException(ErrorCode.INVALID_SETTING_VALUE);
                    }

                    // Chốt chặn 2: Nếu là tỷ lệ cọc thì chặn thêm điều kiện nghiệp vụ không được vượt quá 100%
                    if ("DEPOSIT_PERCENT".equals(setting.getSettingKey()) && numberValue > 100) {
                        throw new BusinessException(ErrorCode.INVALID_SETTING_VALUE);
                    }

                } catch (NumberFormatException e) {
                    // Nếu kiểu cấu hình là NUMBER mà Admin cố tình nhập chữ -> Chặn đứng lập tức, tránh crash tính toán
                    throw new BusinessException(ErrorCode.INVALID_SETTING_VALUE);
                }
            }

            // Trường hợp B: Cấu hình yêu cầu kiểu logic (BOOLEAN) - Ví dụ: Chế độ bảo trì
            else if ("BOOLEAN".equalsIgnoreCase(dataType)) {
                // Chỉ cho phép chuỗi "true" hoặc "false" (không phân biệt chữ hoa chữ thường)
                if (!"true".equalsIgnoreCase(newValue) && !"false".equalsIgnoreCase(newValue)) {
                    throw new BusinessException(ErrorCode.INVALID_SETTING_VALUE);
                }
            }

            // Thực hiện ghi đè giá trị mới vào Entity sau khi đã vượt qua các màng lọc an toàn
            setting.setSettingValue(newValue);
        }

        // 3. Lưu trực tiếp giá trị ghi đè xuống DB (Nhập là lưu)
        SystemSetting updated = settingRepository.save(setting);

        // 4. Đóng gói dữ liệu trả về cho Controller đổi ra JSON (Đã sạch bóng trường is_active)
        return SystemSettingResponse.builder()
                .id(updated.getId())
                .settingKey(updated.getSettingKey())
                .settingValue(updated.getSettingValue())
                .category(updated.getCategory())
                .dataType(updated.getDataType())
                .description(updated.getDescription())
                .build();
    }

    private SystemSettingResponse convertToResponseDTO(SystemSetting setting) {
        return SystemSettingResponse.builder()
                .id(setting.getId())
                .settingKey(setting.getSettingKey())
                .settingValue(setting.getSettingValue())
                .category(setting.getCategory())
                .dataType(setting.getDataType())
                .description(setting.getDescription())
                .build();
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
