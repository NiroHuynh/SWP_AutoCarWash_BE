package com.swp.autocarwash.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileRequest {
    @NotBlank(message = "Trường này không được để trống")
    private String firstName;

    @NotBlank(message = "Trường này không được để trống")
    private String lastName;

    @PastOrPresent(message = "Ngày sinh không được là ngày tương lai")
    private LocalDate birthday;

    @Pattern(regexp = "^(Male|Female|Other)$", message = "Giới tính không hợp lệ")
    private String gender;

    private String location;
}
