package com.swp.autocarwash.auth.controller;

import com.swp.autocarwash.auth.dto.request.ChangePasswordRequest;
import com.swp.autocarwash.auth.service.PasswordService;
import com.swp.autocarwash.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    // Sử dụng PATCH chuẩn RESTful cho việc cập nhật một phần dữ liệu
    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request){
        passwordService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success("Change password successfully",null));
    }
}
