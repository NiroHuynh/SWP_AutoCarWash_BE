package com.swp.autocarwash.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotBlank(message = "Mật khẩu hiện tại không được để trống.")
    private String currentPassword;
    @NotBlank(message = "Mật khẩu mới không được để trống.")
    //Ít nhất 8 ký tự, 1 hoa, 1 thường, 1 số
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
            message = "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và chữ số"
    )
    private String newPassword;
    @NotBlank(message = "Mật khẩu xác nhận không được để trống.")
    private String confirmNewPassword;
}
