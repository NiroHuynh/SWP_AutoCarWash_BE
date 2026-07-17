package com.swp.autocarwash.staff.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSummaryResponse {
    private long totalEmployees;
    private long newThisMonth;
}
