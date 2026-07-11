package com.swp.autocarwash.customer.service.customer.impl;

import com.swp.autocarwash.auth.dto.request.UpdateProfileRequest;
import com.swp.autocarwash.auth.entity.User;
import com.swp.autocarwash.auth.repository.UserRepository;
import com.swp.autocarwash.booking.dto.response.CustomerBookingHistoryItemResponse;
import com.swp.autocarwash.booking.dto.response.CustomerBookingHistoryPageResponse;
import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.dto.response.CustomerDetailResponse;
import com.swp.autocarwash.customer.dto.response.CustomerListItemResponse;
import com.swp.autocarwash.customer.dto.response.CustomerListPageResponse;
import com.swp.autocarwash.customer.dto.response.CustomerProfileResponse;
import com.swp.autocarwash.customer.dto.response.CustomerSummaryResponse;
import com.swp.autocarwash.customer.dto.response.CustomerUpdateProfileResponse;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.FamilyMember;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.mapper.CustomerMapper;
import com.swp.autocarwash.customer.repository.CustomerListProjection;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.customer.repository.FamilyMemberRepository;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.customer.service.customer.CustomerService;
import com.swp.autocarwash.customer.validator.CustomerValidator;
import com.swp.autocarwash.loyalty.entity.CustomerTier;
import com.swp.autocarwash.loyalty.repository.CustomerTierRepository;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.repository.FamilySubscriptionRepository;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 *
 * Chức năng: CustomerServiceImpl triển khai các nghiệp vụ liên quan đến customer.
 * Class này chịu trách nhiệm xử lý logic lấy thông tin customer, kiểm tra điều kiện
 * customer trước khi thực hiện các chức năng như booking.
 *
 * @author Phong
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {



    private final CustomerRepository repository;

    private final CustomerMapper mapper;

    private final CustomerValidator validator;

    private final CustomerRepository customerRepository;
    private final CustomerTierRepository customerTierRepository;
    private final VehicleRepository vehicleRepository;
    private final UnlimitSubscriptionRepository unlimitSubscriptionRepository;
    private final FamilySubscriptionRepository familySubscriptionRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;


    /**
     *
     * Chức năng: Lấy thông tin customer theo id.
     *
     * Quy trình:
     * - Nhận customer id cần tìm kiếm.
     * - Chuyển đổi id sang kiểu dữ liệu phù hợp với repository.
     * - Tìm kiếm customer trong database.
     * - Nếu không tồn tại customer thì ném ResourceNotFoundException.
     * - Trả về Customer entity tìm được.
     *
     * @param id id của customer cần lấy thông tin
     *
     * @return Customer entity chứa thông tin customer
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id){

        Customer customer =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        ErrorCode.CUSTOMER_NOT_FOUND
                                )
                        );

        return customer;

    }





    /**
     *
     * Chức năng: Kiểm tra customer có đủ điều kiện để thực hiện booking hay không.
     *
     * Quy trình:
     * - Nhận customer id cần kiểm tra.
     * - Tìm kiếm customer trong database.
     * - Nếu customer không tồn tại thì throw ResourceNotFoundException.
     * - Gửi customer qua CustomerValidator để kiểm tra các rule nghiệp vụ.
     * - Nếu validate thành công thì customer được phép booking.
     *
     * @param id id của customer cần kiểm tra điều kiện booking
     *
     * @return true nếu customer hợp lệ để booking
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isEligibleForBooking(Long id){

        Customer customer =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        ErrorCode.CUSTOMER_NOT_FOUND
                                )
                        );


        validator.validateBooking(customer);


        return true;
    }

    @Override
    public Customer getCustomerByUserId(Long userId) {
        return repository.findCustomerByUserId(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                ErrorCode.CUSTOMER_NOT_FOUND
                        )
                );
    }


    @Transactional
    @Override
    public void updateTier(
            Long customerId,
            Integer customerTierId
    ) {

        Customer customer =
                repository.findById(customerId)
                        .orElseThrow();

        customer.setCustomerTier(CustomerTier.builder().id(customerTierId).build());
    }

    @Override
    public CustomerProfileResponse getCustomerProfile(Long customerId) {
        // 1. Tìm thông tin Customer dưới DB (Đã được map join sẵn với bảng User lấy Email/Phone)
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND));

        // 2. Tính toán điểm Loyalty và phân hạng thứ bậc (Tier) kế tiếp
        String currentTierName = customer.getCustomerTier() != null ? customer.getCustomerTier().getTierName() : "Member";
        int currentPoints = customer.getLoyaltyBalance().getTotalPoints()!= null ? customer.getLoyaltyBalance().getTotalPoints() : 0;

        String nextTierName = null;
        Integer nextTierMinPoints = null;
        Integer pointsToNextTier = null;

        // Dùng cơ chế hàm Top 1 (Lấy dòng đầu) để tìm mốc điểm hạng tiếp theo

        Optional<CustomerTier> nextTierOpt = customerTierRepository.findTop1ByMinPointsGreaterThanOrderByMinPointsAsc(currentPoints);
        if (nextTierOpt.isPresent()) {
            CustomerTier nextTier = nextTierOpt.get();
            nextTierName = nextTier.getTierName();
            nextTierMinPoints = nextTier.getMinPoints();
            pointsToNextTier = nextTierMinPoints - currentPoints;
        } // Nếu đạt đỉnh Rank (Diamond/Platinum cao nhất) -> Các trường tự động giữ null đúng chuẩn AC

        CustomerProfileResponse.TierInfo tierInfo = CustomerProfileResponse.TierInfo.builder()
                .currentTierName(currentTierName)
                .currentPoints(currentPoints)
                .nextTierName(nextTierName)
                .nextTierMinPoints(nextTierMinPoints)
                .pointsToNextTier(pointsToNextTier)
                .build();

        // 3. Xử lý danh sách phương tiện sở hữu & Quét chéo gói Membership ACTIVE
        List<Vehicle> dbVehicles = vehicleRepository.findByCustomerIdAndIsDeletedFalse(customerId);
        List<CustomerProfileResponse.VehicleInfo> vehicleDTOs = new ArrayList<>();

        for (Vehicle vehicle : dbVehicles) {
            // Dựng khung DTO thô cho từng chiếc xe
            CustomerProfileResponse.VehicleInfo.VehicleInfoBuilder vehicleBuilder = CustomerProfileResponse.VehicleInfo.builder()
                    .id(vehicle.getId())
                    .licensePlate(vehicle.getLicensePlate())
                    .brandName(vehicle.getBrandName())
                    .color(vehicle.getColor());

            //PHÂN NHÁNH THỬ NGHIỆM 1: Kiểm tra xem xe có đăng ký gói cá nhân UNLIMITED không
            Optional<UnlimitSubscription> unlimitSubOpt = unlimitSubscriptionRepository.findActiveUnlimitedSubByVehicleId(vehicle.getId());

            if (unlimitSubOpt.isPresent()) {
                UnlimitSubscription sub = unlimitSubOpt.get();
                CustomerProfileResponse.ActiveSubscriptionInfo.ActiveSubscriptionInfoBuilder subBuilder =
                        CustomerProfileResponse.ActiveSubscriptionInfo.builder()
                                .type(sub.getSubscriptionPlan().getPlanType()); // Trả về chữ "UNLIMITED"

                // Tính toán cửa sổ thời gian khóa 30 ngày (Lock Period) của gói Unlimited
                LocalDateTime lastChange = sub.getLastVehicleChangeAt();
                if (lastChange != null) {
                    LocalDateTime unlockDateTime = lastChange.plusDays(30);
                    //cứ 30 ngày cho đổi 1 lần
                    LocalDateTime now = LocalDateTime.now();

                    // Nếu thời điểm hiện tại chưa vượt qua ngày mở khóa -> Đóng dấu hasTransferred = true
                    if (now.isBefore(unlockDateTime)) {
                        subBuilder.hasTransferred(true);
                        subBuilder.transferUnlockDate(unlockDateTime.toLocalDate());
                    }
                }
                vehicleBuilder.activeSubscription(subBuilder.build());

            }
            //PHÂN NHÁNH THỬ NGHIỆM 2: Nếu không dính gói cá nhân, kiểm tra quyền lợi từ gói nhóm FAMILY
            else {
                Optional<FamilySubscription> familySubOpt = familySubscriptionRepository.findActiveFamilySubByVehicleId(vehicle.getId());

                if (familySubOpt.isPresent()) {
                    FamilySubscription sub = familySubOpt.get();
                    CustomerProfileResponse.ActiveSubscriptionInfo.ActiveSubscriptionInfoBuilder subBuilder =
                            CustomerProfileResponse.ActiveSubscriptionInfo.builder()
                                    .type(sub.getSubscriptionPlan().getPlanType()); // Trả về chữ "FAMILY"

                    // Lội ngược vào bảng nhật ký family_member của xe để xem mốc thời gian đổi xe gia đình
                    Optional<FamilyMember> familyMemberOpt = familyMemberRepository.findByVehicleId(vehicle.getId());
                    if (familyMemberOpt.isPresent()) {
                        FamilyMember memberLog = familyMemberOpt.get();
                        LocalDateTime windowStart = memberLog.getVehicleChangeWindowStart();

                        if (windowStart != null) {
                            LocalDateTime unlockDateTime = windowStart.plusDays(30);
                            //cứ 30 ngày cho đổi xe 1 lần
                            LocalDateTime now = LocalDateTime.now();

                            if (now.isBefore(unlockDateTime)) {
                                subBuilder.hasTransferred(true);
                                subBuilder.transferUnlockDate(unlockDateTime.toLocalDate());
                            }
                        }
                    }
                    vehicleBuilder.activeSubscription(subBuilder.build());
                }
                // Nếu rớt ra ngoài cả 2 nhánh -> Xe không có gói -> activeSubscription mang giá trị null -> Jackson tự động xóa Key rác khỏi JSON
            }

            vehicleDTOs.add(vehicleBuilder.build());
        }

        // 4. Đồng bộ dữ liệu đóng thùng trả về kết quả
        CustomerProfileResponse.ProfileData profileData = CustomerProfileResponse.ProfileData.builder()
                .customer(CustomerProfileResponse.CustomerInfo.builder()
                        .id(customer.getId())
                        .firstName(customer.getFirstName())
                        .lastName(customer.getLastName())
                        .birthday(customer.getBirthday())
                        .email(customer.getUser().getEmail())
                        .phone(customer.getUser().getPhone())
                        .location(null) // đang trả về null vì trong bảng customer không có lưu địa chỉ
                        .build())
                .tier(tierInfo)
                .vehicles(vehicleDTOs) // Trả về mảng rỗng [] nếu khách chưa đăng ký xe nào (Đúng chuẩn mẫu 2 của em)
                .build();

        return CustomerProfileResponse.builder()
                .message("Get profile successfully")
                .data(profileData)
                .build();
    }

    @Override
    @Transactional //Đảm bảo tính ACID, tự động Rollback nếu quá trình lưu bị lỗi DB
    public CustomerUpdateProfileResponse updateCustomerProfile(Long customerId, UpdateProfileRequest request) {
        // 1. Tìm Customer dưới DB
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));

        // 2. Cập nhật thông tin từ DTO vào Entity
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setBirthday(request.getBirthday());

        User user = customer.getUser();
        // 3. Lưu lại xuống Database
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerUpdateProfileResponse.builder()
                .id(savedCustomer.getId())
                .phone(user.getPhone())
                .email(user.getEmail())
                .birthday(savedCustomer.getBirthday())
                .lastName(savedCustomer.getLastName())
                .firstName(savedCustomer.getFirstName()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerListPageResponse getCustomerList(
            String keyword, Integer year, Integer month, String tier, Boolean active, Integer stationId,
            Integer communeId, Integer provinceId, Pageable pageable) {
        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime monthEnd = monthStart.plusMonths(1);

        CustomerSummaryResponse summary = CustomerSummaryResponse.builder()
                .totalCustomers(customerRepository.countQualifiedCustomers())
                .newThisMonth(customerRepository.countNewThisMonth(monthStart, monthEnd))
                .goldMembers(customerRepository.countGoldMembers())
                .build();

        Page<CustomerListProjection> page = customerRepository.findCustomerList(
                keyword, year, month, tier, active, stationId, communeId, provinceId, pageable);

        List<CustomerListItemResponse> content = page.getContent().stream()
                .map(p -> CustomerListItemResponse.builder()
                        .customerId(p.getCustomerId())
                        .fullName(p.getFullName())
                        .email(p.getEmail())
                        .phone(p.getPhone())
                        .active(p.getActive())
                        .tier(p.getTier())
                        .lastVisit(p.getLastVisit())
                        .build())
                .toList();

        return CustomerListPageResponse.builder()
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
    public CustomerDetailResponse getCustomerDetail(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
        User user = customer.getUser();
        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        long noShowCount = bookingRepository.countNoShowByCustomerId(customerId);
        boolean inactive = !Boolean.TRUE.equals(user.getIsActive()) || noShowCount >= 3;

        Integer totalPoints = customer.getLoyaltyBalance() != null
                ? customer.getLoyaltyBalance().getTotalPoints()
                : 0;

        List<CustomerDetailResponse.VehicleInfo> vehicles =
                vehicleRepository.findByCustomerIdAndIsDeletedFalse(customerId).stream()
                        .map(v -> CustomerDetailResponse.VehicleInfo.builder()
                                .brandName(v.getBrandName())
                                .color(v.getColor())
                                .licensePlate(v.getLicensePlate())
                                .activeSubscriptionType(resolveActiveSubscriptionType(v.getId()))
                                .build())
                        .toList();

        return CustomerDetailResponse.builder()
                .customerId(customer.getId())
                .customerCode("CUST-" + String.format("%05d", customer.getId()))
                .fullName(customer.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .tier(customer.getCustomerTier() != null ? customer.getCustomerTier().getTierName() : null)
                .totalPoints(totalPoints)
                .lastVisit(bookingRepository.findLastCheckOutByCustomerId(customerId))
                .accountStatus(inactive ? "INACTIVE" : "ACTIVE")
                .vehicles(vehicles)
                .build();
    }

    @Override
    @Transactional
    public List<String> deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));

        List<String> plates = bookingRepository.findActiveBookingsByCustomerId(customerId).stream()
                .map(b -> b.getVehicle().getLicensePlate())
                .distinct()
                .toList();
        if (!plates.isEmpty()) {
            return plates;
        }

        User user = customer.getUser();
        user.setIsDeleted(true);
        userRepository.save(user);
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerBookingHistoryPageResponse getCustomerBookingHistory(
            Long customerId, String vehicleKeyword, Integer serviceCategoryId,
            String status, Integer stationId, Integer year, Integer month, Pageable pageable) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));

        // AC2: từ khóa phương tiện chỉ áp dụng khi >= 2 ký tự, ngắn hơn thì bỏ qua filter
        String keyword = vehicleKeyword != null && vehicleKeyword.trim().length() >= 2
                ? vehicleKeyword.trim()
                : null;

        // AC4: status phải là 1 giá trị hợp lệ của BookingStatus, tránh FE gửi sai mà không biết
        if (status != null) {
            try {
                BookingStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new BusinessException(ErrorCode.INVALID_REQUEST);
            }
        }

        Page<Booking> page = bookingRepository.findCustomerBookingHistory(
                customerId, keyword, serviceCategoryId, status, stationId, year, month, pageable);

        List<CustomerBookingHistoryItemResponse> content = page.getContent().stream()
                .map(b -> CustomerBookingHistoryItemResponse.builder()
                        .bookingId(b.getId())
                        .appointmentDate(b.getAppointmentDate())
                        .serviceCategoryName(b.getServicePackage().getServiceCategory().getCategoryName())
                        .licensePlate(b.getVehicle().getLicensePlate())
                        .brandName(b.getVehicle().getBrandName())
                        .status(b.getStatus())
                        .totalAmount(b.getTotalAmount())
                        .staffName(b.getCheckInEmployee() != null ? b.getCheckInEmployee().getFullName() : null)
                        .build())
                .toList();

        return CustomerBookingHistoryPageResponse.builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private String resolveActiveSubscriptionType(Long vehicleId) {
        return unlimitSubscriptionRepository.findActiveUnlimitedSubByVehicleId(vehicleId)
                .map(sub -> sub.getSubscriptionPlan().getPlanType())
                .or(() -> familySubscriptionRepository.findActiveFamilySubByVehicleId(vehicleId)
                        .map(sub -> sub.getSubscriptionPlan().getPlanType()))
                .orElse(null);
    }

}
