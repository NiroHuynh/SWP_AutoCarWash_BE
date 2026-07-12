package com.swp.autocarwash.customer.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CustomerUpdateProfileResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String email;
    private String phone;
}
