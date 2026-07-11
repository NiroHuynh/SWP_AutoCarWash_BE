package com.swp.autocarwash.customer.service.family.impl;

import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.dto.request.CreateFamilyGroupRequest;
import com.swp.autocarwash.customer.dto.response.CreateFamilyGroupResponse;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.FamilyGroup;
import com.swp.autocarwash.customer.entity.FamilyMember;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.customer.repository.FamilyGroupRepository;
import com.swp.autocarwash.customer.repository.FamilyMemberRepository;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.customer.service.family.FamilyGroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FamilyGroupServiceImpl implements FamilyGroupService {

    private final FamilyGroupRepository familyGroupRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final SecurityUtils securityUtils;

    @Override
    public Long getOwnerCustomerIdOfCustomerId(Long customerId) {
        if (customerId == null) {
            return null;
        }
        return familyGroupRepository.findOwnerCustomerIdByMemberCustomerId(customerId);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public CreateFamilyGroupResponse createFamilyGroup(CreateFamilyGroupRequest request) {

        // 1. CHỐT CHẶN AC02: Validate dữ liệu đầu vào cơ bản
        if (request.getGroupName() == null || request.getGroupName().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.GROUP_NAME_CANNOT_BE_EMPTY);
        }

        if (request.getGroupName().length() > 100) {
            throw new BusinessException(ErrorCode.GROUP_NAME_TOO_LONG);
        }

        if (request.getVehicleId() == null) {
            throw new BusinessException(ErrorCode.VEHICLE_REQUIRED);
        }

        // 2. Trích xuất danh tính Customer từ Token JWT
        Long userId = securityUtils.getCurrentUserId();
        Customer currentCustomer = customerRepository.findByUserId(userId);
        if (currentCustomer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        Long customerId = currentCustomer.getId();

        // 3. CHỐT CHẶN AC03: Kiểm tra xem khách hàng đã sở hữu hoặc tham gia nhóm nào chưa
        boolean isOwnerOfAnyGroup = familyGroupRepository.existsByOwnerCustomerIdAndIsDeletedFalse(customerId);
        boolean isMemberOfAnyGroup = familyMemberRepository.existsByCustomerId(customerId);

        if (isOwnerOfAnyGroup || isMemberOfAnyGroup) {
            throw new BusinessException(ErrorCode.CUSTOMER_ALREADY_HAS_FAMILY_GROUP);
        }

        // 4. CHỐT CHẶN AC04: Kiểm tra tính hợp lệ và độc quyền của xe
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId()).orElse(null);

        // Kiểm tra xe có tồn tại và thuộc quyền sở hữu của chính khách hàng này không
        if (vehicle == null || Boolean.TRUE.equals(vehicle.getIsDeleted()) || !vehicle.getCustomer().getId().equals(customerId)) {
            throw new BusinessException(ErrorCode.VEHICLE_NOT_BELONG_TO_CUSTOMER);
        }

        // Kiểm tra xem chiếc xe này đã bị trói ở một nhóm gia đình nào khác chưa
        boolean isVehicleLinked = familyMemberRepository.existsByVehicleId(request.getVehicleId());
        if (isVehicleLinked) {
            throw new BusinessException(ErrorCode.VEHICLE_ALREADY_IN_ANOTHER_GROUP);
        }

        // =========================================================================
        // THỰC THI GHI DỮ LIỆU ĐỒNG THỜI (AC05) - NẾU CÓ LỖI SẼ TỰ ĐỘNG ROLLBACK
        // =========================================================================

        // Bước 1: Khởi tạo và lưu bảng cha family_group
        FamilyGroup familyGroup = FamilyGroup.builder()
                .groupName(request.getGroupName().trim())
                .ownerCustomer(currentCustomer)
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        familyGroup = familyGroupRepository.save(familyGroup);

        // Bước 2: Tự động nạp chủ nhóm làm thành viên (FamilyMember) đầu tiên với chiếc xe đã chọn
        FamilyMember ownerMember = FamilyMember.builder()
                .familyGroup(familyGroup)
                .customer(currentCustomer)
                .vehicle(vehicle) // Dùng luôn object vehicle đã tìm thấy ở bước 4
                .vehicleChangeCount(0)
                .build();

        familyMemberRepository.save(ownerMember);

        // 5. Đóng gói kết quả trả về đúng định dạng API Contract SUCCESS
        return CreateFamilyGroupResponse.builder()
                .familyGroupId(familyGroup.getId())
                .groupName(familyGroup.getGroupName())
                .ownerCustomerId(customerId)
                .build();
    }
    
}
