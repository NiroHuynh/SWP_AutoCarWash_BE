package com.swp.autocarwash.common.contract.customer;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerContract {


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

}