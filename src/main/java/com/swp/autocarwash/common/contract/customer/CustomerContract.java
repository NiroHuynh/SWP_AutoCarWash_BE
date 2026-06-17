package com.swp.autocarwash.common.contract.customer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CustomerContract {

    private Integer id;
    private Integer userId;

    private String firstName;
    private String lastName;

    private Integer violationCount;
    private LocalDateTime restrictedUntil;
}
