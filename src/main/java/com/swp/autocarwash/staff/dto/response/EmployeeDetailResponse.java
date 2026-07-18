package com.swp.autocarwash.staff.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailResponse {
    private Long employeeId;
    private String employeeCode;
    private String fullName;
    /** Tách riêng first/last name để form Edit ở FE seed được 2 ô input. */
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer stationId;
    private String stationName;
    /** "ACTIVE" hoặc "INACTIVE", map từ User.isActive. */
    private String accountStatus;
    private LocalDateTime createdAt;
}
