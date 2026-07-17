package com.swp.autocarwash.staff.service;

import com.swp.autocarwash.staff.dto.request.UpdateEmployeeRequest;
import com.swp.autocarwash.staff.dto.response.EmployeeDetailResponse;
import com.swp.autocarwash.staff.dto.response.EmployeeListPageResponse;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeListPageResponse getEmployeeList(
            String keyword, Boolean active, Integer stationId, Integer communeId, Integer provinceId, Pageable pageable);

    EmployeeDetailResponse getEmployeeDetail(Long employeeId);

    /**
     * Chức năng: Admin sửa thông tin nhân viên (tên trên bảng staff; email/phone/
     * trạng thái trên bảng user; điều chuyển chi nhánh qua station_id).
     */
    EmployeeDetailResponse updateEmployee(Long employeeId, UpdateEmployeeRequest request);

    /**
     * Chức năng: Admin xoá nhân viên - soft delete qua User.isDeleted, giữ nguyên
     * lịch sử booking mà nhân viên này từng check-in.
     */
    void deleteEmployee(Long employeeId);
}
