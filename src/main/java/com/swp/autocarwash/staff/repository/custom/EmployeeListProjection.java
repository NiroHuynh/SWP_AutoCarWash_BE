package com.swp.autocarwash.staff.repository.custom;

import java.time.LocalDateTime;

public interface EmployeeListProjection {
    Long getEmployeeId();
    String getFullName();
    String getEmail();
    String getPhone();
    Integer getStationId();
    String getStationName();
    Boolean getActive();
    LocalDateTime getCreatedAt();
}
