package com.swp.autocarwash.system.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.system.dto.request.CreateSettingRequest;
import com.swp.autocarwash.system.dto.request.UpdateSettingRequest;
import com.swp.autocarwash.system.dto.response.SystemSettingResponse;
import com.swp.autocarwash.system.service.SystemSettingService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/system-settings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class SystemSettingController {

    private final SystemSettingService settingService;

    // API lấy danh sách gom nhóm
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, List<SystemSettingResponse>>>> getAllSettings() {
        Map<String, List<SystemSettingResponse>> data = settingService.getAllSettingsGrouped();
        return ResponseEntity.ok(ApiResponse.<Map<String, List<SystemSettingResponse>>>builder()
                .success(true)
                .message("The list of system configurations by category has been successfully downloaded.")
                .data(data)
                .build());
    }

    // API Thêm cấu hình mới
    @PostMapping
    public ResponseEntity<ApiResponse<SystemSettingResponse>> createSetting(
            @RequestBody CreateSettingRequest request) {
        SystemSettingResponse data = settingService.createSetting(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<SystemSettingResponse>builder()
                .success(true)
                .message("System configuration created successfully.")
                .data(data)
                .build());
    }

    // API Cập nhật giá trị ghi đè (Nhập là lưu)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SystemSettingResponse>> updateSetting(
            @PathVariable Long id,
            @RequestBody UpdateSettingRequest request) {
        SystemSettingResponse data = settingService.updateSetting(id, request);
        return ResponseEntity.ok(ApiResponse.<SystemSettingResponse>builder()
                .success(true)
                .message("System configuration updated successfully.")
                .data(data)
                .build());
    }

}
