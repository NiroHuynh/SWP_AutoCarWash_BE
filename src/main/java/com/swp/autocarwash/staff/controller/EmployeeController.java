package com.swp.autocarwash.staff.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.staff.dto.request.CreateEmployeeRequest;
import com.swp.autocarwash.staff.dto.request.UpdateEmployeeRequest;
import com.swp.autocarwash.staff.dto.response.EmployeeDetailResponse;
import com.swp.autocarwash.staff.dto.response.EmployeeListPageResponse;
import com.swp.autocarwash.staff.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Chức năng: Admin xem danh sách nhân viên, kèm 2 thẻ KPI (Total Employees, New This Month).
     *
     * @param active     lọc theo trạng thái tài khoản (true=hoạt động/false=đã khoá), bỏ trống = không lọc
     * @param stationId  lọc theo chi nhánh cụ thể, bỏ trống = không lọc
     * @param communeId  lọc theo xã/phường, bỏ trống = không lọc
     * @param provinceId lọc theo tỉnh/thành, bỏ trống = không lọc
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeListPageResponse>> getEmployeeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Integer stationId,
            @RequestParam(required = false) Integer communeId,
            @RequestParam(required = false) Integer provinceId) {
        Pageable pageable = PageRequest.of(page, size);
        EmployeeListPageResponse data = employeeService.getEmployeeList(
                keyword, active, stationId, communeId, provinceId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Employee list", data));
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeDetailResponse>> getEmployeeDetail(@PathVariable Long employeeId) {
        EmployeeDetailResponse data = employeeService.getEmployeeDetail(employeeId);
        return ResponseEntity.ok(ApiResponse.success("Employee detail", data));
    }

    /**
     * Chức năng: Admin tạo nhân viên mới từ form Add New - tạo luôn tài khoản đăng
     * nhập (role STAFF) với password do admin đặt.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeDetailResponse>> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {
        EmployeeDetailResponse data = employeeService.createEmployee(request);
        return ResponseEntity.ok(ApiResponse.success("Employee created successfully", data));
    }

    /**
     * Chức năng: Admin sửa thông tin nhân viên từ popup chi tiết.
     */
    @PutMapping("/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeDetailResponse>> updateEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody UpdateEmployeeRequest request) {
        EmployeeDetailResponse data = employeeService.updateEmployee(employeeId, request);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", data));
    }

    /**
     * Chức năng: Admin xoá nhân viên (soft delete qua User.isDeleted).
     */
    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully", null));
    }
}
