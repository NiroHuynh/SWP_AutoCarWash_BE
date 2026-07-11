package com.swp.autocarwash.customer.service.family.impl;

import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.dto.request.AddFamilyMemberRequest;
import com.swp.autocarwash.customer.dto.request.CreateFamilyGroupRequest;
import com.swp.autocarwash.customer.dto.request.SearchInvitedCustomerResponse;
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
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.repository.FamilySubscriptionRepository;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyGroupServiceImpl implements FamilyGroupService {

    private final FamilyGroupRepository familyGroupRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final SecurityUtils securityUtils;

    private final FamilySubscriptionRepository familySubscriptionRepository;
    private final UnlimitSubscriptionRepository unlimitSubscriptionRepository;

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

    @Override
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')") // Chỉ tài khoản Khách hàng mới có quyền gọi luồng này
    public void addFamilyMember(AddFamilyMemberRequest request) {
        LocalDate today = LocalDate.now();

        // =========================================================================
        // VÒNG GÁC CỔNG 1: XÁC THỰC QUYỀN CHỦ NHÓM (SECURITY GUARD)
        // =========================================================================
        Long userId = securityUtils.getCurrentUserId();
        Customer currentCustomer = customerRepository.findByUserId(userId);
        if (currentCustomer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        FamilyGroup familyGroup = familyGroupRepository.findById(request.getFamilyGroupId())
                .orElseThrow(() -> new BusinessException(ErrorCode.FAMILY_GROUP_NOT_FOUND));

        // Kiểm tra xem ông đang bấm nút gửi request có thực sự là Owner của nhóm này không
        if (!familyGroup.getOwnerCustomer().getId().equals(currentCustomer.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NOT_GROUP_OWNER);
        }

        // =========================================================================
        // VÒNG GÁC CỔNG 2: KIỂM TRA TÌNH TRẠNG GÓI CƯỚC & HẠN MỨC NHÓM (AC07 + AC08)
        // =========================================================================

        // AC07: Quét gói đăng ký Active của nhóm
        FamilySubscription activeSub = familySubscriptionRepository
                .findActiveSubscription(request.getFamilyGroupId(), today)
                .orElseThrow(() -> new BusinessException(ErrorCode.FAMILY_SUBSCRIPTION_EXPIRED_OR_NOT_FOUND));

        // Bốc cấu hình định mục gói ra để lấy số xe tối đa (max_vehicle_count)
        int maxVehicleLimit = activeSub.getSubscriptionPlan().getMaxVehicleCount();

        // AC08: Đếm số lượng thành viên hiện tại trong nhóm
        long currentMemberCount = familyMemberRepository.countByFamilyGroupId(request.getFamilyGroupId());
        if (currentMemberCount >= maxVehicleLimit) {
            throw new BusinessException(ErrorCode.FAMILY_GROUP_LIMIT_EXCEEDED);
        }

        // =========================================================================
        // VÒNG GÁC CỔNG 3: VALIDATE NGƯỜI ĐƯỢC MỜI & PHƯƠNG TIỆN (AC02 -> AC06)
        // =========================================================================

        // AC02: Chặn không cho tự thêm chính mình
        if (request.getInvitedCustomerId().equals(currentCustomer.getId())) {
            throw new BusinessException(ErrorCode.CANNOT_INVITE_YOURSELF);
        }

        Customer invitedCustomer = customerRepository.findById(request.getInvitedCustomerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVITED_CUSTOMER_NOT_FOUND));

        // AC04: Người được mời không được tham gia nhóm khác
        boolean isInvitedOwner = familyGroupRepository.existsByOwnerCustomerIdAndIsDeletedFalse(request.getInvitedCustomerId());
        boolean isInvitedMember = familyMemberRepository.existsByCustomerId(request.getInvitedCustomerId());
        if (isInvitedOwner || isInvitedMember) {
            throw new BusinessException(ErrorCode.INVITED_CUSTOMER_ALREADY_IN_ANOTHER_GROUP);
        }

        // AC03 + AC05: Kiểm tra xe hợp lệ và thuộc sở hữu của người được mời
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new BusinessException(ErrorCode.VEHICLE_NOT_FOUND));

        if (Boolean.TRUE.equals(vehicle.getIsDeleted()) || !vehicle.getCustomer().getId().equals(invitedCustomer.getId())) {
            throw new BusinessException(ErrorCode.VEHICLE_NOT_BELONG_TO_CUSTOMER);
        }

        // Kiểm tra xem xe này đã nằm trong bảng family_member của nhóm nào khác chưa
        boolean isVehicleInFamily = familyMemberRepository.existsByVehicleId(request.getVehicleId());
        if (isVehicleInFamily) {
            throw new BusinessException(ErrorCode.VEHICLE_ALREADY_IN_ANOTHER_GROUP);
        }

        // AC06 CHỐT CHẶN VÁ GIAN LẬN: Xe không được vướng gói cước cá nhân còn hạn
        boolean hasPersonalSub = unlimitSubscriptionRepository.hasActivePersonalSubscription(request.getVehicleId(), today);
        if (hasPersonalSub) {
            throw new BusinessException(ErrorCode.VEHICLE_HAS_ACTIVE_PERSONAL_SUBSCRIPTION);
        }

        // =========================================================================
        // THỰC THI LƯU THÀNH VIÊN VÀO DATABASE (AC09)
        // =========================================================================
        FamilyMember newMember = FamilyMember.builder()
                .familyGroup(familyGroup)
                .customer(invitedCustomer)
                .vehicle(vehicle)
                .vehicleChangeCount(0) // Khởi tạo chu kỳ đổi xe bằng 0
                .build();

        familyMemberRepository.save(newMember);
    }

    @Override
    @PreAuthorize("hasRole('CUSTOMER')")
    public SearchInvitedCustomerResponse searchInvitedCustomer(String identifier) {

        if (identifier == null || identifier.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.IDENTIFIER_CANNOT_BE_EMPTY);
        }

        // 1. Lấy thông tin ông đang thực hiện tìm kiếm để check chống tự mời chính mình (AC02)
        Long userId = securityUtils.getCurrentUserId();
        Customer currentCustomer = customerRepository.findByUserId(userId);

        // 2. Tìm kiếm người được mời theo Email/SĐT dưới DB
        Customer invitedCustomer = customerRepository.findByIdentifier(identifier.trim())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVITED_CUSTOMER_NOT_FOUND)); // Trả về lỗi: Khách hàng không tồn tại

        // Chốt chặn AC02: Kiểm tra nếu tự tìm kiếm chính mình
        if (invitedCustomer.getId().equals(currentCustomer.getId())) {
            throw new BusinessException(ErrorCode.CANNOT_INVITE_YOURSELF);
        }

        // 3. Bốc danh sách xe chưa bị xóa của người được mời (AC03)
        List<Vehicle> vehicles = vehicleRepository.findByCustomerIdAndIsDeletedFalse(invitedCustomer.getId());

        List<SearchInvitedCustomerResponse.VehicleDto> vehicleDtos = vehicles.stream()
                .map(v -> SearchInvitedCustomerResponse.VehicleDto.builder()
                        .id(v.getId())
                        .licensePlate(v.getLicensePlate())
                        .brandName(v.getBrandName())
                        .color(v.getColor())
                        .build())
                .toList();

        // 4. Đóng gói trả về cho FE hiển thị thông tin và nạp xe vào Dropdown
        return SearchInvitedCustomerResponse.builder()
                .customerId(invitedCustomer.getId())
                .fullName(invitedCustomer.getLastName() + " " + invitedCustomer.getFirstName())
                .phone(invitedCustomer.getUser().getPhone()) // Hoặc invitedCustomer.getPhone() tùy DB của em
                .email(invitedCustomer.getUser().getEmail())
                .vehicles(vehicleDtos)
                .build();
    }

}
