package com.swp.autocarwash.staff.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Body của POST /api/employees - admin tạo nhân viên mới.
 *
 * Giống UpdateEmployeeRequest nhưng có thêm password: tạo nhân viên là tạo cả
 * tài khoản đăng nhập, mà User.passwordHash là NOT NULL. Dự án chưa có module
 * gửi mail nên không thể sinh password rồi email cho nhân viên - admin tự đặt
 * và báo lại cho họ.
 *
 * Lưu ý: message của các annotation validation BẮT BUỘC phải là tên hằng trong
 * ErrorCode, vì GlobalExceptionHandler.handleValidation() gọi
 * ErrorCode.valueOf(defaultMessage). Ghi câu chữ thường vào đây sẽ khiến
 * valueOf() ném IllegalArgumentException -> API trả 500 thay vì 400.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEmployeeRequest {

    @NotBlank(message = "EMPLOYEE_FIRST_NAME_REQUIRED")
    @Size(max = 100, message = "EMPLOYEE_FIRST_NAME_REQUIRED")
    private String firstName;

    @NotBlank(message = "EMPLOYEE_LAST_NAME_REQUIRED")
    @Size(max = 100, message = "EMPLOYEE_LAST_NAME_REQUIRED")
    private String lastName;

    @NotBlank(message = "EMPLOYEE_EMAIL_INVALID")
    @Email(message = "EMPLOYEE_EMAIL_INVALID")
    private String email;

    @NotBlank(message = "EMPLOYEE_PHONE_INVALID")
    @Pattern(regexp = "^(0[0-9]{9})$", message = "EMPLOYEE_PHONE_INVALID")
    private String phone;

    @NotNull(message = "EMPLOYEE_BRANCH_REQUIRED")
    private Integer stationId;

    @NotNull(message = "EMPLOYEE_STATUS_REQUIRED")
    private Boolean active;

    /** Password thô, hash bằng passwordEncoder trước khi lưu. Tối thiểu 6 ký tự như luồng register. */
    @NotBlank(message = "EMPLOYEE_PASSWORD_INVALID")
    @Size(min = 6, message = "EMPLOYEE_PASSWORD_INVALID")
    private String password;
}
