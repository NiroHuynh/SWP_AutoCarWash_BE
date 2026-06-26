package com.swp.autocarwash.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {


    /**
     * First name of customer
     */
    @NotBlank(message = "First name is required")
    private String firstName;


    /**
     * Last name of customer
     */
    @NotBlank(message = "Last name is required")
    private String lastName;


    /**
     * Customer birthday
     */
    private LocalDate birthday;


    /**
     * User phone number
     */
    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^(0[0-9]{9})$",
            message = "Phone number is invalid"
    )
    private String phone;


    /**
     * User email
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;


    /**
     * Raw password before hashing
     */
    @NotBlank(message = "Password is required")
    @Size(
            min = 6,
            message = "Password must have at least 6 characters"
    )
    private String password;

}