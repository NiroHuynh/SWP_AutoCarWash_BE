package com.swp.autocarwash.customer.service.family.impl;

import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.dto.request.AddFamilyMemberRequest;
import com.swp.autocarwash.customer.dto.request.CreateFamilyGroupRequest;
import com.swp.autocarwash.customer.dto.request.SearchInvitedCustomerResponse;
import com.swp.autocarwash.customer.dto.response.*;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final BookingRepository bookingRepository;

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

    @Override
    @Transactional(readOnly = true)
    public FamilyGroupDetailsResponse getFamilyGroupDetails() {
        // 1. Giải mã Token lấy userId của người đang đăng nhập
        Long userId = securityUtils.getCurrentUserId();
        Customer currentCustomer = customerRepository.findByUserId(userId);
        if (currentCustomer == null) {
            throw new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        Long currentCustomerId = currentCustomer.getId();
        FamilyGroup targetGroup = null;
        boolean isOwner = false;

        //Định vị nhóm
        //Optional<FamilyGroup> ownedGroupOpt = familyGroupRepository.findByOwnerCustomerId(currentCustomerId);
        Optional<FamilyGroup> ownedGroupOpt = familyGroupRepository.findByOwnerCustomerIdAndIsDeletedFalse(currentCustomerId);
        if (ownedGroupOpt.isPresent()) {
            targetGroup = ownedGroupOpt.get();
            isOwner = true;
        } else {
            // Nếu không phải Owner -> Tìm thẳng trong bảng family_member xem có tồn tại không
            Optional<FamilyMember> memberOpt = familyMemberRepository.findByCustomerId(currentCustomerId);
            if (memberOpt.isPresent()) {
                targetGroup = memberOpt.get().getFamilyGroup();
                isOwner = false;
            }
        }

        // LUỒNG AC01: Nếu quét cả 2 bảng mà không ra nhóm nào -> Trả về null để FE hiện nút Tạo nhóm
        if (targetGroup == null) {
            return null;
        }

        // 3. Bốc thông tin gói cước của nhóm (AC02)
        GroupSubscriptionDto subscriptionDto = null;
        int maxVehicleCount = 0;

        //Optional<FamilySubscription> activeSubOpt = familySubscriptionRepository.findActiveSubscriptionByGroupId(targetGroup.getId());
        Optional<FamilySubscription> latestSubOpt = familySubscriptionRepository.findLatestSubscriptionByGroupId(targetGroup.getId());
//        if (activeSubOpt.isPresent()) {
//            FamilySubscription sub = activeSubOpt.get();
//            maxVehicleCount = sub.getSubscriptionPlan().getMaxVehicleCount();
//
//            subscriptionDto = GroupSubscriptionDto.builder()
//                    .planName(sub.getSubscriptionPlan().getPlanName())
//                    .status(sub.getStatus())
//                    .endDate(sub.getEndDate())
//                    .build();
//        }

        if (latestSubOpt.isPresent()) {
            FamilySubscription sub = latestSubOpt.get();

            // Nếu gói thực tế đang ACTIVE dưới DB thì lấy hạn mức, nếu đã thành EXPIRED thì hạn mức đưa về 0
            //if ("ACTIVE".equalsIgnoreCase(sub.getStatus())) {
                maxVehicleCount = sub.getSubscriptionPlan().getMaxVehicleCount();
            //} else {
             //   maxVehicleCount = 0;
            //}

            subscriptionDto = GroupSubscriptionDto.builder()
                    .planName(sub.getSubscriptionPlan().getPlanName())
                    .status(sub.getStatus()) //Trả thẳng chữ ACTIVE / EXPIRED chuẩn bài từ DB ra cho FE
                    .endDate(sub.getEndDate())
                    .build();
        }

        // 4. HỢP NHẤT DANH SÁCH THÀNH VIÊN VÀ XE LIÊN KẾT (AC03)
        List<GroupMemberDto> memberList = new ArrayList<>();

        //Dòng đầu tiên: Nạp thông tin của ông Chủ nhóm (Owner)
        Customer owner = targetGroup.getOwnerCustomer();

        //ĐÃ CẬP NHẬT: Vì xe của Owner nằm trong bảng family_member, quét thẳng tại đây
        Optional<FamilyMember> ownerMemberOpt = familyMemberRepository.findByCustomerId(owner.getId());
        Vehicle ownerVehicle = ownerMemberOpt.isPresent() ? ownerMemberOpt.get().getVehicle() : null;

        memberList.add(GroupMemberDto.builder()
                .customerId(owner.getId())
                .fullName(owner.getLastName() + " " + owner.getFirstName())
                .email(owner.getUser().getEmail())
                .phone(owner.getUser().getPhone())
                .roleInGroup("OWNER") // Găm tag Chủ nhóm để FE nhận diện
                .linkedVehicle(ownerVehicle == null ? null : GroupMemberDto.LinkedVehicleDto.builder()
                                                             .vehicleId(ownerVehicle.getId())
                                                             .licensePlate(ownerVehicle.getLicensePlate())
                                                             .brandName(ownerVehicle.getBrandName())
                                                             .color(ownerVehicle.getColor())
                                                             .build())
                .build());

        //Các dòng tiếp theo: Lấy tất cả thành viên phụ trong nhóm nối đuôi vào danh sách
        List<FamilyMember> familyMembers = familyMemberRepository.findByFamilyGroupId(targetGroup.getId());
        for (FamilyMember fm : familyMembers) {
            Customer memberCust = fm.getCustomer();

            //ĐÃ THÊM CHỐT CHẶN: Bỏ qua chính ông Chủ nhóm vì đã nạp ông ấy ở dòng đầu tiên rồi
            if (memberCust.getId().equals(owner.getId())) {
                continue;
            }

            Vehicle memberVehicle = fm.getVehicle();
            memberList.add(GroupMemberDto.builder()
                    .customerId(memberCust.getId())
                    .fullName(memberCust.getLastName() + " " + memberCust.getFirstName())
                    .email(memberCust.getUser().getEmail())
                    .phone(memberCust.getUser().getPhone())
                    .roleInGroup("MEMBER") // Găm tag Thành viên phụ
                    .linkedVehicle(memberVehicle == null ? null : GroupMemberDto.LinkedVehicleDto.builder()
                                                                  .vehicleId(memberVehicle.getId())
                                                                  .licensePlate(memberVehicle.getLicensePlate())
                                                                  .brandName(memberVehicle.getBrandName())
                                                                  .color(memberVehicle.getColor())
                                                                  .build())
                    .build());
        }

        // 5. Tính toán chuỗi tỷ lệ hạn mức "Hiện tại / Tối đa" đổ vào DTO gói cước
        if (subscriptionDto != null) {
            subscriptionDto.setUsageSummary(memberList.size() + "/" + maxVehicleCount);
        }

        return FamilyGroupDetailsResponse.builder()
                .familyGroupId(targetGroup.getId())
                .groupName(targetGroup.getGroupName())
                .createdAt(targetGroup.getCreatedAt())
                .isOwner(isOwner)
                .subscription(subscriptionDto)
                .members(memberList)
                .build();

    }

    @Override
    public RemoveMemberResponse removeMember(Long memberCustomerId) {

        // 1. SECURITY GUARD: Giải mã Token lấy customerId của người đang thực hiện hành động
        Long userId = securityUtils.getCurrentUserId();
        Customer currentCustomer = customerRepository.findByUserId(userId);
        if (currentCustomer == null) {
            throw new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        Long currentCustomerId = currentCustomer.getId();

        // 2. Kiểm tra xem người gọi có phải là Owner của nhóm nào không
        FamilyGroup ownedGroup = familyGroupRepository.findByOwnerCustomerId(currentCustomerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED_ACTION));
        // Không phải Owner -> Chặn đứng ngay lập tức (AC02)

        // 3. Kiểm tra xem thành viên phụ sắp bị xóa có thực sự nằm trong nhóm của Owner này không
        FamilyMember memberToDelete = familyMemberRepository.findByCustomerId(memberCustomerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!memberToDelete.getFamilyGroup().getId().equals(ownedGroup.getId())) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
            // Thành viên tồn tại nhưng thuộc nhóm nhà người khác -> Báo lỗi không tìm thấy
        }

        // Chặn luồng nếu Owner tự xóa chính mình bằng API này (Owner muốn rời nhóm phải dùng luồng giải tán nhóm riêng)
        if (memberCustomerId.equals(currentCustomerId)) {
            throw new BusinessException(ErrorCode.INVALID_ACTION);
        }

        // 4. BOOKING GUARD (AC03): Kiểm tra lịch đặt dở dang của chiếc xe gắn liền với thành viên đó
        if (memberToDelete.getVehicle() != null) {
            Long memberVehicleId = memberToDelete.getVehicle().getId();
            List<String> unfinishedStatuses = List.of(BookingStatus.PENDING.name(), BookingStatus.CONFIRMED.name(), BookingStatus.CHECK_IN.name(), BookingStatus.WASHING.name(), BookingStatus.CONFIRMED.name());

            boolean hasUnfinishedBooking = bookingRepository.existsByVehicleIdAndStatusIn(memberVehicleId, unfinishedStatuses);
            if (hasUnfinishedBooking) {
                throw new BusinessException(ErrorCode.VEHICLE_HAS_ACTIVE_BOOKING);
                // Xe đang rửa hoặc có lịch hẹn chưa xong -> Chặn đứng, quăng lỗi nghiệp vụ
            }
        }

        // 5. HARD DELETE (AC04): Thực thi xóa vật lý dòng dữ liệu khỏi bảng family_member
        familyMemberRepository.delete(memberToDelete);

        // Sau lệnh này, liên kết xe biến mất -> Mọi quyền lợi tự động bị bẻ gãy, ra quầy tự tính tiền mặt

        // 6. ĐẾM VÀ TÍNH TOÁN HẠN MỨC MỚI (AC05)
        // Lấy danh sách thành viên còn lại
        List<FamilyMember> remainingMembers = familyMemberRepository.findByFamilyGroupId(ownedGroup.getId());

        // Không +1 nữa vì Owner đã nằm sẵn trong remainingMembers
        int currentCount = remainingMembers.size();

        // Tìm số xe tối đa được phép của gói ACTIVE để làm mẫu số
        int maxVehicleCount = 0;
        Optional<FamilySubscription> activeSubOpt = familySubscriptionRepository.findActiveSubscriptionByGroupId(ownedGroup.getId());
        if (activeSubOpt.isPresent()) {
            maxVehicleCount = activeSubOpt.get().getSubscriptionPlan().getMaxVehicleCount();
        }

        String newUsageSummary = currentCount + "/" + maxVehicleCount;

        // 7. Đóng gói dữ liệu trả về theo đúng API Contract
        return RemoveMemberResponse.builder()
                .familyGroupId(ownedGroup.getId())
                .removedCustomerId(memberCustomerId)
                .newUsageSummary(newUsageSummary)
                .build();
    }

    @Override
    @Transactional
    public DissolveGroupResponse dissolveFamilyGroup() {
        // 1. SECURITY GUARD: Lấy thông tin người dùng từ Token JWT
        Long userId = securityUtils.getCurrentUserId();
        Customer currentCustomer = customerRepository.findByUserId(userId);
        if (currentCustomer == null) {
            throw new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        Long currentCustomerId = currentCustomer.getId();

        // 2. PHÂN QUYỀN: Tìm xem khách hàng này có phải là Owner của nhóm nào không
        FamilyGroup targetGroup = familyGroupRepository.findByOwnerCustomerId(currentCustomerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED_ACTION));
        // Không phải Owner -> Bắn lỗi 403 Forbidden lập tức (AC01/AC02)

        // 3. BOOKING GUARD (AC03): Gom toàn bộ xe trong nhóm để check lịch dở dang
        List<FamilyMember> allMembers = familyMemberRepository.findByFamilyGroupId(targetGroup.getId());

        // Trích xuất danh sách ID xe của toàn bộ nhóm (Bao gồm cả xe của Owner và Member phụ)
        List<Long> groupVehicleIds = new ArrayList<>();
        for (FamilyMember member : allMembers) {
            if (member.getVehicle() != null) {
                groupVehicleIds.add(member.getVehicle().getId());
            }
        }

        if (!groupVehicleIds.isEmpty()) {
            List<String> unfinishedStatuses = List.of(BookingStatus.PENDING.name(), BookingStatus.CONFIRMED.name(), BookingStatus.CHECK_IN.name(), BookingStatus.WASHING.name(), BookingStatus.COMPLETED.name());
            boolean hasActiveBookings = bookingRepository.existsByVehicleIdInAndStatusIn(groupVehicleIds, unfinishedStatuses);

            if (hasActiveBookings) {
                throw new BusinessException(ErrorCode.GROUP_HAS_ACTIVE_BOOKINGS);
                // Có ít nhất 1 xe đang rửa hoặc chờ rửa -> Chặn đứng, bắt xử lý xong lịch mới cho hủy nhóm
            }
        }

        // 4. VÔ HIỆU HÓA GÓI CƯỚC (Hủy gói liên kết của nhóm)
//        Optional<FamilySubscription> activeSubOpt = familySubscriptionRepository.findActiveSubscriptionByGroupId(targetGroup.getId());
//        String subStatus = "NO_SUBSCRIPTION";
//        if (activeSubOpt.isPresent()) {
//            FamilySubscription subscription = activeSubOpt.get();
//            subscription.setStatus("CANCELED");
//            subscription.setCanceledAt(LocalDateTime.now());
//            familySubscriptionRepository.save(subscription);
//            subStatus = "CANCELED";
//        }
        Optional<FamilySubscription> latestSubOpt = familySubscriptionRepository.findLatestSubscriptionByGroupId(targetGroup.getId());
        String subStatus = "NO_SUBSCRIPTION";
        if (latestSubOpt.isPresent()) {
            FamilySubscription subscription = latestSubOpt.get();

            // Nếu gói dưới DB vốn dĩ đã hết hạn (EXPIRED) từ trước do Cron Job quét
            if ("EXPIRED".equalsIgnoreCase(subscription.getStatus())) {
                subStatus = "EXPIRED"; // Giữ nguyên trạng thái hiển thị, không thay đổi DB nữa
            }
            else if ("ACTIVE".equalsIgnoreCase(subscription.getStatus())) {
                // Gói thực sự đang còn hạn sử dụng -> Tiến hành cập nhật CANCELED (Hủy gói) xuống DB
                subscription.setStatus("CANCELED");
                subscription.setCanceledAt(LocalDateTime.now());
                familySubscriptionRepository.save(subscription);
                subStatus = "CANCELED";
            }
        }




        // 5. HARD DELETE (AC04): Xóa cứng toàn bộ thành viên + Owner ra khỏi bảng family_member
        int kickedCount = allMembers.size();
        familyMemberRepository.deleteByFamilyGroupId(targetGroup.getId());
        // Lệnh này bẻ gãy mọi sợi xích liên kết xe, đưa tất cả thành viên về trạng thái "vô gia cư" để tự do đi nhóm khác

        // 6. SOFT DELETE GROUP (AC04): Ẩn nhóm gia đình này đi
        targetGroup.setIsDeleted(true);
        familyGroupRepository.save(targetGroup);

        // 7. Trả kết quả về cho Controller đổ ra JSON (AC05)
        return DissolveGroupResponse.builder()
                .familyGroupId(targetGroup.getId())
                .ownerCustomerId(currentCustomerId)
                .totalMembersKicked(kickedCount)
                .subscriptionStatus(subStatus)
                .build();
    }

}
