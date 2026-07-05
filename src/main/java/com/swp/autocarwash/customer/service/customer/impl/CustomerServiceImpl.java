package com.swp.autocarwash.customer.service.customer.impl;

import com.swp.autocarwash.auth.dto.request.UpdateProfileRequest;
import com.swp.autocarwash.auth.entity.User;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.dto.response.CustomerProfileResponse;
import com.swp.autocarwash.customer.dto.response.CustomerUpdateProfileResponse;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.FamilyMember;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.mapper.CustomerMapper;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.customer.repository.FamilyMemberRepository;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.customer.service.customer.CustomerService;
import com.swp.autocarwash.customer.validator.CustomerValidator;
import com.swp.autocarwash.loyalty.entity.CustomerTier;
import com.swp.autocarwash.loyalty.repository.custom.CustomerTierRepository;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.repository.FamilySubscriptionRepository;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
