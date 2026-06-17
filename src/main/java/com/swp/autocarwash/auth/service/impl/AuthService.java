package com.swp.autocarwash.auth.service.impl;

import com.nimbusds.jose.JOSEException;
import com.swp.autocarwash.auth.dto.request.LoginRequest;

public interface AuthService {

    //Hàm xử lý đăng nhập, nhận vào DTO và trả về chuỗi thông báo tạm thời
    String login(LoginRequest request) throws JOSEException;
}
