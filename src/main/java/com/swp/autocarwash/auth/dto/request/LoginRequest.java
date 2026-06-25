package com.swp.autocarwash.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    //sử dụng thư viện bean validation để bắt
    @NotBlank(message= "Tài khoản không được để trống" )
    private String identity;    //tk là email or phone
    @NotBlank(message= "Mật khẩu không được để trống")
    private String password;
}
