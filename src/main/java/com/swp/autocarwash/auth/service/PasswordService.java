package com.swp.autocarwash.auth.service;

import com.swp.autocarwash.auth.dto.request.ChangePasswordRequest;

public interface PasswordService {
    void changePassword(ChangePasswordRequest request);
}
