package com.swp.autocarwash.common.contract.customer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * Object dùng để giao tiếp giữa các module
 *
 * Không expose Entity ra ngoài module
 *
 * @author Phong
 * @version 1.0
 */
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
