package com.swp.autocarwash.common.contract.customer;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerContract {
    
      private Integer id;

    /**
     * User id from auth module
     */
    private Integer userId;


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

    public CustomerContract(Integer userId) {
        this.userId = userId;
    }
}
