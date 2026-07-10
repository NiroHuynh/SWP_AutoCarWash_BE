package com.swp.autocarwash.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Chức năng: 1 dòng trong bảng Quản lý khách hàng (FE-US-09 AC2) — Tên, Email,
 * SĐT, Trạng thái, Lần ghé cuối.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerListItemResponse {

    private Long customerId;

    private String fullName;

    private String email;

    private String phone;

    private Boolean active;

    private String tier;

    private LocalDateTime lastVisit;
}
