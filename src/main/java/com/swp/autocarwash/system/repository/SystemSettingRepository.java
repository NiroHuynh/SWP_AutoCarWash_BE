package com.swp.autocarwash.system.repository;

import com.swp.autocarwash.system.entity.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {
    Optional<SystemSetting> findBySettingKey(String settingKey);

    Optional<SystemSetting> findSystemSettingBySettingKeyAndIsActiveTrue(String settingKey);

    // Tìm tất cả cấu hình đang kích hoạt
    List<SystemSetting> findByIsActiveTrue();

    // Kiểm tra trùng Key để chặn luồng Create
    boolean existsBySettingKey(String settingKey);
}
