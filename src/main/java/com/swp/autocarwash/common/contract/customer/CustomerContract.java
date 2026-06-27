package com.swp.autocarwash.common.contract.customer;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalDate;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerContract {

    private Long id;
    private Long userId;


    /**
     * Customer first name
     */
    private String firstName;


    /**
     * Customer last name
     */
    private String lastName;


    /**
     * Customer birthday
     */
    private LocalDate birthday;

      private Integer violationCount;
    private LocalDateTime restrictedUntil;

    public CustomerContract(Long userId) {
        this.userId = userId;
    }
}
