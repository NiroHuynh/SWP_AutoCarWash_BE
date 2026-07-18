package com.swp.autocarwash.staff.service.impl;

import com.swp.autocarwash.auth.entity.Role;
import com.swp.autocarwash.auth.entity.User;
import com.swp.autocarwash.auth.entity.enums.UserRole;
import com.swp.autocarwash.auth.repository.RoleRepository;
import com.swp.autocarwash.auth.repository.UserRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.staff.dto.request.CreateEmployeeRequest;
import com.swp.autocarwash.staff.dto.request.UpdateEmployeeRequest;
import com.swp.autocarwash.staff.dto.response.EmployeeDetailResponse;
import com.swp.autocarwash.staff.dto.response.EmployeeListItemResponse;
import com.swp.autocarwash.staff.dto.response.EmployeeListPageResponse;
import com.swp.autocarwash.staff.dto.response.EmployeeSummaryResponse;
import com.swp.autocarwash.staff.entity.Staff;
import com.swp.autocarwash.staff.repository.custom.EmployeeListProjection;
import com.swp.autocarwash.staff.repository.custom.StaffRepository;
import com.swp.autocarwash.staff.service.EmployeeService;
import com.swp.autocarwash.station.entity.Station;
import com.swp.autocarwash.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public EmployeeListPageResponse getEmployeeList(
            String keyword, Boolean active, Integer stationId, Integer communeId, Integer provinceId, Pageable pageable) {
        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime monthEnd = monthStart.plusMonths(1);

        EmployeeSummaryResponse summary = EmployeeSummaryResponse.builder()
                .totalEmployees(staffRepository.countQualifiedEmployees(stationId, communeId, provinceId))
                .newThisMonth(staffRepository.countNewThisMonth(monthStart, monthEnd, stationId, communeId, provinceId))
                .build();

        Page<EmployeeListProjection> page = staffRepository.findEmployeeList(
                keyword, active, stationId, communeId, provinceId, pageable);

        List<EmployeeListItemResponse> content = page.getContent().stream()
                .map(p -> EmployeeListItemResponse.builder()
                        .employeeId(p.getEmployeeId())
                        .employeeCode(String.valueOf(p.getEmployeeId()))
                        .fullName(p.getFullName())
                        .email(p.getEmail())
                        .phone(p.getPhone())
                        .stationId(p.getStationId())
                        .stationName(p.getStationName())
                        .active(p.getActive())
                        .createdAt(p.getCreatedAt())
                        .build())
                .toList();

        return EmployeeListPageResponse.builder()
                .summary(summary)
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDetailResponse getEmployeeDetail(Long employeeId) {
        Staff staff = staffRepository.findEmployeeDetailById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND));

        return toDetailResponse(staff);
    }

    @Override
    @Transactional
    public EmployeeDetailResponse createEmployee(CreateEmployeeRequest request) {
        // Chưa có id để loại trừ nên dùng existsByEmail/existsByPhone như luồng register,
        // không phải bản ...AndIdNot của update.
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        Role staffRole = roleRepository.findByName(UserRole.ROLE_STAFF.name())
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_FOUND));

        Station station = stationRepository.findByIdAndIsDeletedFalse(request.getStationId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.STATION_NOT_FOUND));

        User user = User.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(staffRole)
                .isActive(request.getActive())
                .createdAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);

        // Staff không có @Builder -> new + setter, giống cách register dựng Customer.
        Staff staff = new Staff();
        staff.setUser(user);
        staff.setStation(station);
        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staffRepository.save(staff);

        // employeeCode suy ra từ staff.id nên phải save xong mới map được.
        return toDetailResponse(staff);
    }

    @Override
    @Transactional
    public EmployeeDetailResponse updateEmployee(Long employeeId, UpdateEmployeeRequest request) {
        Staff staff = staffRepository.findEmployeeDetailById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND));
        User user = staff.getUser();

        // Loại trừ chính user đang sửa, nếu không họ giữ nguyên email/phone cũ sẽ bị
        // báo trùng với chính mình.
        if (userRepository.existsByEmailAndIdNot(request.getEmail(), user.getId())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhoneAndIdNot(request.getPhone(), user.getId())) {
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        Station station = stationRepository.findByIdAndIsDeletedFalse(request.getStationId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.STATION_NOT_FOUND));

        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setStation(station);
        staffRepository.save(staff);

        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setIsActive(request.getActive());
        userRepository.save(user);

        return toDetailResponse(staff);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long employeeId) {
        Staff staff = staffRepository.findEmployeeDetailById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND));

        // Soft delete: chỉ đánh dấu trên User, giữ nguyên dòng staff vì Booking
        // .checkInEmployee còn trỏ tới (lịch sử ai từng check-in). Các query trong
        // StaffRepository đều lọc u.isDeleted = false nên nhân viên tự biến mất khỏi
        // list lẫn detail.
        User user = staff.getUser();
        user.setIsDeleted(true);
        user.setIsActive(false);
        userRepository.save(user);
    }

    private EmployeeDetailResponse toDetailResponse(Staff staff) {
        User user = staff.getUser();
        return EmployeeDetailResponse.builder()
                .employeeId(staff.getId())
                .employeeCode(String.valueOf(staff.getId()))
                .fullName(staff.getFullName())
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .stationId(staff.getStation().getId())
                .stationName(staff.getStation().getStationName())
                .accountStatus(Boolean.TRUE.equals(user.getIsActive()) ? "ACTIVE" : "INACTIVE")
                .createdAt(user.getCreatedAt())
                .build();
    }
}
