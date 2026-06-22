package com.swp.autocarwash.common.contract.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class CustomerContract {

    private Long id;
    private Long userId;

    private String firstName;
    private String lastName;

    private Integer violationCount;
    private LocalDateTime restrictedUntil;
}
