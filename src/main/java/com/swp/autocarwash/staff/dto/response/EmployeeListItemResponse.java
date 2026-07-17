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
public class EmployeeListItemResponse {
    private Long employeeId;
    private String employeeCode;
    private String fullName;
    private String email;
    private String phone;
    private Integer stationId;
    private String stationName;
    private Boolean active;
    private LocalDateTime createdAt;
}
