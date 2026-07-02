package com.swp.autocarwash.system.repository;

import com.swp.autocarwash.system.entity.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {
    Optional<SystemSetting> findBySettingKey(String settingKey);


    Optional<SystemSetting> findSystemSettingBySettingKeyAndIsActiveTrue(String settingKey);
}
