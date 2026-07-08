package com.swp.autocarwash.customer.repository;

import java.time.LocalDateTime;

/**
 * Chức năng: Projection cho 1 dòng trong bảng Quản lý khách hàng (FE-US-09) —
 * gồm các field cột hiển thị cộng "Lần ghé cuối" là aggregate (MAX checkOutAt).
 */
public interface CustomerListProjection {
    Long getCustomerId();
    String getFullName();
    String getEmail();
    String getPhone();
    Boolean getActive();
    String getTier();
    LocalDateTime getLastVisit();
}
