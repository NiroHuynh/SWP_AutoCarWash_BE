package com.swp.autocarwash.customer.service.vehicle.impl;

import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.customer.dto.request.CreateVehicleRequest;
import com.swp.autocarwash.customer.dto.request.TransferPlanRequest;
import com.swp.autocarwash.customer.dto.request.UpdateVehicleRequest;
import com.swp.autocarwash.customer.dto.response.CreateVehicleResponse;
import com.swp.autocarwash.customer.dto.response.UpdateVehicleResponse;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.FamilyMember;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.mapper.VehicleMapper;
import com.swp.autocarwash.customer.port.CustomerPort;
import com.swp.autocarwash.customer.repository.FamilyMemberRepository;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.customer.service.vehicle.VehicleService;
import com.swp.autocarwash.customer.validator.VehicleValidator;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.mapper.VehicleMapper;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.customer.service.vehicle.VehicleService;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.repository.FamilySubscriptionRepository;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import com.swp.autocarwash.system.entity.SystemSetting;
import com.swp.autocarwash.system.repository.SystemSettingRepository;
import com.swp.autocarwash.system.service.SystemSettingService;
import com.swp.autocarwash.system.service.impl.SystemSettingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;



import java.util.List;

/**
 *
 * Chức năng: VehicleServiceImpl triển khai các nghiệp vụ xử lý vehicle.
 * Class này chịu trách nhiệm quản lý logic lấy danh sách xe, lấy thông tin chi tiết
 * vehicle và kiểm tra quyền sở hữu vehicle trước khi sử dụng trong các flow nghiệp vụ.
 *
 * @author Phong
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    private final VehicleValidator vehicleValidator;

    private final VehicleMapper vehicleMapper;

    private final CustomerPort customerPort;

    private final BookingRepository bookingRepository;

    private final UnlimitSubscriptionRepository unlimitSubscriptionRepository;

    private final FamilySubscriptionRepository familySubscriptionRepository;

    private final SystemSettingServiceImpl systemSettingServiceImpl;

    private final FamilyMemberRepository familyMemberRepository;
    private final SystemSettingRepository systemSettingRepository;

    /**
     *
     * Chức năng: Lấy danh sách vehicle đang hoạt động của một customer.
     *
     * Quy trình:
     * - Nhận customerId cần lấy danh sách vehicle.
     * - Truy vấn vehicle theo customerId và loại bỏ vehicle đã bị xóa.
     * - Kiểm tra danh sách vehicle có tồn tại hay không.
     * - Nếu không có vehicle thì ném BusinessException.
     * - Mapping danh sách Vehicle entity sang VehicleContract.
     * - Trả về danh sách vehicle của customer.
     *
     * @param customerId id của customer cần lấy danh sách vehicle
     *
     * @return danh sách VehicleContract thuộc customer
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<VehicleContract> getVehiclesByCustomer(Long customerId) {

        List<Vehicle> vehicles =
                vehicleRepository.findByCustomerIdAndIsDeletedFalse(customerId);

        if (vehicles.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_VEHICLE_REGISTERED);
        }

        return vehicleMapper.toContracts(vehicles);
    }

    /**
     * Create vehicle
     *
     * Flow:
     * 1. Tìm xe theo biển số xem đã tồn tại chưa (vd xe walk-in cũ chưa có chủ)
     * 2. Validate: nếu biển số đã có chủ (customer khác null) -> báo lỗi trùng
     * 3. Lấy customer tương ứng với userId đang đăng nhập (qua CustomerPort)
     * 4a. Nếu xe đã tồn tại nhưng chưa có chủ -> gắn customer vào xe cũ (UPDATE),
     *     không tạo dòng mới, để giữ nguyên lịch sử (booking, review...) đang gắn theo vehicle_id cũ
     * 4b. Nếu chưa có xe nào với biển số này -> tạo vehicle mới, field FE không gửi
     *     (violationCount, restrictedUntil, isDeleted) -> set mặc định
     * 5. Save
     * 6. Return response
     */
    @Override
    @Transactional
    public CreateVehicleResponse createVehicle(Long userId, CreateVehicleRequest request) {

        Customer customer = customerPort.getCustomerReferenceByUserId(userId);

        String licensePlate = request.getLicensePlate().trim().toUpperCase();

        Optional<Vehicle> vehicleOpt =
                vehicleRepository.findByLicensePlateIgnoreCase(licensePlate);

        Vehicle vehicle;

        if (vehicleOpt.isPresent()) {

            vehicle = vehicleOpt.get();

            // Xe đang được sử dụng bởi customer khác
            if (Boolean.FALSE.equals(vehicle.getIsDeleted())
                    && vehicle.getCustomer() != null) {

                throw new BusinessException(ErrorCode.LICENSE_PLATE_ALREADY_EXISTS);
            }

            // Xe tồn tại nhưng chưa có chủ -> gán customer
            if (Boolean.FALSE.equals(vehicle.getIsDeleted())
                    && vehicle.getCustomer() == null) {

                vehicle.setCustomer(customer);
                vehicle.setBrandName(request.getBrandName());
                vehicle.setColor(request.getColor());

                vehicle = vehicleRepository.save(vehicle);

                return vehicleMapper.toResponse(vehicle);
            }
        }


         vehicle = Vehicle.builder()
                .customer(customer)
                .licensePlate(licensePlate)   // dùng bản đã normalize
                .brandName(request.getBrandName())
                .color(request.getColor())
                .violationCount(0)
                .restrictedUntil(null)
                .isDeleted(false)
                .build();

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponse(savedVehicle);
    }

    @Transactional
    @Override
    public void deleteVehicle(Long customerId, Long vehicleId) {

            //Tìm xe theo id và xe đó chưa bị xoá trước đây
            Vehicle vehicle = vehicleRepository.findByIdAndIsDeletedFalse(vehicleId).orElseThrow(
                    () -> new ResourceNotFoundException(ErrorCode.VEHICLE_NOT_FOUND)
            );
            //Secur: check xem xe này có đúng là của khách hàng đang đăng nhập không
            if(!vehicle.getCustomer().getId().equals(customerId)){
                throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS_VEHICLE);
            }
            //kiểm tra xem xe có thoả mãn trạng thái để xoá không
            // nếu đang tồn tại trong booking ở pending, confirmed, check-in, washing thì không được xoá
            if(bookingRepository.hasActiveBooking(vehicleId)){
                throw new BusinessException(ErrorCode.VEHICLE_HAS_ACTIVE_BOOKING);
            }

            LocalDate today = LocalDate.now();

            boolean hasActiveUnlimited = unlimitSubscriptionRepository
                .findActiveSubscriptionByVehicle(vehicleId, today).isPresent();

            // Check gói Family hoạt động
            boolean hasActiveFamily = familySubscriptionRepository
                .findActiveFamilySubscriptionByVehicle(vehicleId, today).isPresent();

            if (hasActiveUnlimited || hasActiveFamily) {
                throw new BusinessException(ErrorCode.VEHICLE_HAS_ACTIVE_SUBSCRIPTION);
        }
            //tiến hành xoá mềm
            vehicle.setIsDeleted(true);
            vehicleRepository.save(vehicle);
    }

    @Transactional
    @Override
    public UpdateVehicleResponse updateVehicle(Long customerId, Long vehicleId, UpdateVehicleRequest request) {
        //tìm vehicle theo id nếu k có thì trả ra exception
        Vehicle vehicle = vehicleRepository.findByIdAndIsDeletedFalse(vehicleId).orElseThrow(
                () -> new ResourceNotFoundException(ErrorCode.VEHICLE_NOT_FOUND)
        );

        //check vehicle này thuộc về customer account này hay không
        if(!vehicle.getCustomer().getId().equals(customerId)){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS_VEHICLE);
        }

        //check xem thử license_plate mới đã tồn tại ở vehicle khác chưa
        if (request.getLicensePlate() != null) {
            String newPlate = request.getLicensePlate().trim().toUpperCase();
            // Validate qua validator: cho phép nếu xe cùng biển đã bị xóa mềm
            vehicleValidator.validateUpdate(newPlate, vehicleId);
            request.setLicensePlate(newPlate); // chuẩn hóa luôn trước khi save
        }

        //update vào bảng vehicle
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setBrandName(request.getBrandName());
        vehicle.setColor(request.getColor());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return UpdateVehicleResponse.builder()
                .id(savedVehicle.getId())
                .licensePlate(savedVehicle.getLicensePlate())
                .brandName(savedVehicle.getBrandName())
                .color(savedVehicle.getColor())
                .build();
    }

    @Transactional
    @Override
    public void transferSubscription(Long customerId, TransferPlanRequest request) {
        //validate source vehicle
        Vehicle vehicle = vehicleRepository.findByIdAndIsDeletedFalse(request.getSourceVehicleId())
                .orElseThrow( () -> new ResourceNotFoundException(ErrorCode.VEHICLE_NOT_FOUND));
        if(!vehicle.getCustomer().getId().equals(customerId)){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS_VEHICLE);
        }
        //Quét xem xe này đang thuộc gói nào
        Optional<UnlimitSubscription> unlimitOpt = unlimitSubscriptionRepository.findByVehicleIdAndStatus(request.getSourceVehicleId(),
                "ACTIVE");
        Optional<FamilyMember> familyMemberOpt = familyMemberRepository.findActiveFamilyMemberByVehicleId(request.getSourceVehicleId(), LocalDate.now());

        if(unlimitOpt.isEmpty() && familyMemberOpt.isEmpty()){
            throw new BusinessException(ErrorCode.SUBSCRIPTION_NOT_FOUND);
        }
        // Check từng gói một nếu có sở hữu -> lấy unlimitedDay ra và plus vào
        int lockPeriodDays = systemSettingServiceImpl.getTransferLock("SUBSCRIPTION_TRANSFER_LOCK_DAYS");

        //nếu xe sở hữu gói unlimited
//        if(unlimitOpt.isPresent()){
//            UnlimitSubscription unlimitSub = unlimitOpt.get();
//            if(unlimitSub.getLastVehicleChangeAt() != null) {
//                LocalDateTime lockDeadline = unlimitSub.getLastVehicleChangeAt().plusDays(lockPeriodDays);
//
//                if (LocalDateTime.now().isBefore(lockDeadline)) {
//                    throw new BusinessException(ErrorCode.TRANSFER_LIMIT_REACHED);
//                }
//            }
//        }
//        else if(familyMemberOpt.isPresent()){
//            FamilyMember familyMember = familyMemberOpt.get();
//            if(familyMember.getVehicleChangeCount() != null && familyMember.getVehicleChangeCount() >= 1){
//                LocalDateTime lockDeadline = familyMember.getVehicleChangeWindowStart().plusDays(lockPeriodDays);
//                if(LocalDateTime.now().isBefore(lockDeadline)){
//                    throw new BusinessException(ErrorCode.TRANSFER_LIMIT_REACHED);
//                }
//            }
//        }

        // Trích xuất mốc thời gian đổi gần nhất dựa theo gói đang sở hữu
        LocalDateTime lastChangeAt = null;
        if (unlimitOpt.isPresent()) {
            lastChangeAt = unlimitOpt.get().getLastVehicleChangeAt();
        } else if (familyMemberOpt.isPresent()) {
            // Tận dụng trường window start cũ để lưu mốc thời gian đổi gần nhất cho đồng bộ
            lastChangeAt = familyMemberOpt.get().getVehicleChangeWindowStart();
        }

        // Gác cổng: Nếu đã từng đổi xe và thời gian hiện tại vẫn chưa vượt qua hạn lock -> Chặn đứng
        if (lastChangeAt != null) {
            LocalDateTime lockDeadline = lastChangeAt.plusDays(lockPeriodDays);
            if (LocalDateTime.now().isBefore(lockDeadline)) {
                throw new BusinessException(ErrorCode.TRANSFER_LIMIT_REACHED);
            }
        }

        //Check xem xe đang có booking nào ở trạng thái không thoả không
        boolean hasActiveBooking = bookingRepository.hasActiveBooking(request.getSourceVehicleId());
        if(hasActiveBooking){
            throw new BusinessException(ErrorCode.VEHICLE_HAS_ACTIVE_BOOKING);
        }

        //VALID XE ĐÍCH
        Vehicle targetVehicle = vehicleRepository.findByIdAndIsDeletedFalse(request.getTargetVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.VEHICLE_NOT_FOUND));
        if (!targetVehicle.getCustomer().getId().equals(customerId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS_VEHICLE);
        }
        if (Objects.equals(request.getSourceVehicleId(), request.getTargetVehicleId())) {
            throw new BusinessException(ErrorCode.TRANSFER_SAME_VEHICLE);
        }
        LocalDate today = LocalDate.now();
        //check xem xe đích có đang dính gói unli/family không
        boolean isTargetInUnlimit = unlimitSubscriptionRepository.hasActiveSubscription(request.getTargetVehicleId(), today);
        boolean isTargetInFamily = familyMemberRepository.existsActiveFamilyByVehicleId(request.getTargetVehicleId(), today);

        if (isTargetInUnlimit || isTargetInFamily) {
            throw new BusinessException(ErrorCode.TARGET_VEHICLE_HAS_SUBSCRIPTION);
        }

        //PERFORM TRANSFER PLAN (CẬP NHẬT XE MỚI XUỐNG DB)
        // Nhánh A: Cập nhật cho gói Unlimited
        LocalDateTime now = LocalDateTime.now();

        // Nhánh A: Cập nhật cho gói Unlimited
        if (unlimitOpt.isPresent()) {
            UnlimitSubscription unlimitSub = unlimitOpt.get();
            unlimitSub.setVehicle(targetVehicle);
            unlimitSub.setLastVehicleChangeAt(now); // Găm mốc thời gian khóa lần này
            unlimitSubscriptionRepository.save(unlimitSub);
        }
        // Nhánh B: Cập nhật cho gói thành viên Family Group (Đã lược bỏ đếm số lần)
        else if (familyMemberOpt.isPresent()) {
            FamilyMember familyMember = familyMemberOpt.get();
            familyMember.setVehicle(targetVehicle); // Ghi đè xe mới vào hệ thống gia đình
            familyMember.setVehicleChangeWindowStart(now); // Găm mốc thời gian khóa lần này (Đồng bộ với Unlimited)

            // Nếu DB chưa xóa cột count, có thể set cứng tạm thời hoặc bỏ qua dòng dưới nếu đã xóa DB:
            // familyMember.setVehicleChangeCount(1);

            familyMemberRepository.save(familyMember);
        }
    }

    /**
         *
         * Chức năng: Lấy thông tin chi tiết vehicle theo id.
         *
         * Quy trình:
         * - Nhận vehicleId cần tìm.
         * - Tìm kiếm vehicle trong database.
         * - Nếu không tồn tại vehicle thì throw VEHICLE_NOT_FOUND.
         * - Kiểm tra trạng thái vehicle có bị inactive/xóa hay không.
         * - Mapping Vehicle entity sang VehicleContract.
         * - Trả về thông tin vehicle.
         *
         * @param id id của vehicle cần lấy thông tin
         *
         * @return VehicleContract chứa thông tin chi tiết vehicle
         *
         * @author Phong
         * @version 1.0
         */
    @Override
    public VehicleContract getById(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.VEHICLE_NOT_FOUND));

        if (Boolean.TRUE.equals(vehicle.getIsDeleted())) {
            throw new BusinessException(ErrorCode.VEHICLE_INACTIVE);
        }

        return vehicleMapper.toContract(vehicle);
    }

    /**
     *
     * Chức năng: Kiểm tra vehicle có thuộc quyền sở hữu của customer hay không.
     *
     * Quy trình:
     * - Nhận vehicleId và customerId cần xác thực.
     * - Kiểm tra sự tồn tại của quan hệ vehicle - customer trong database.
     * - Nếu vehicle không thuộc customer thì throw VEHICLE_NOT_OWNED.
     * - Trả về kết quả validate.
     *
     * @param vehicleId id của vehicle cần kiểm tra quyền sở hữu
     * @param customerId id của customer cần xác nhận quyền sở hữu
     *
     * @return true nếu vehicle thuộc customer hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    @Transactional(readOnly = true)
    public boolean validateVehicleOwnership(Long vehicleId, Long customerId) {

        boolean exists = vehicleRepository
                .existsByIdAndCustomerId(vehicleId, customerId);

        if (!exists) {
            throw new BusinessException(ErrorCode.VEHICLE_NOT_OWNED);
        }

        return true;
    }

}
