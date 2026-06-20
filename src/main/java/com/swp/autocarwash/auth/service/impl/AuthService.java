package com.swp.autocarwash.auth.service.impl;

import com.nimbusds.jose.JOSEException;
import com.swp.autocarwash.auth.dto.request.LoginRequest;
import com.swp.autocarwash.auth.dto.response.LoginResponse;

public interface AuthService {

    //Hàm xử lý đăng nhập, nhận vào DTO và trả về chuỗi thông báo tạm thời
    LoginResponse login(LoginRequest request) throws JOSEException;
}
