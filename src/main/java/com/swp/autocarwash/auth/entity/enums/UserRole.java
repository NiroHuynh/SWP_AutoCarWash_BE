package com.swp.autocarwash.auth.entity.enums;

/**
 * Tên role tương ứng với cột role.name trong DB.
 * Dùng làm khóa lookup, không liên quan tới role.id (AUTO_INCREMENT).
 */
public enum UserRole {

    ROLE_CUSTOMER,
    ROLE_STAFF,
    ROLE_ADMIN

}