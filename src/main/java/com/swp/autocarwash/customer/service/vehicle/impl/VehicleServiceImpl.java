package com.swp.autocarwash.customer.service.vehicle.impl;

import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.customer.dto.request.CreateVehicleRequest;
import com.swp.autocarwash.customer.dto.request.UpdateVehicleRequest;
import com.swp.autocarwash.customer.dto.response.CreateVehicleResponse;
import com.swp.autocarwash.customer.dto.response.UpdateVehicleResponse;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.mapper.VehicleMapper;
import com.swp.autocarwash.customer.port.CustomerPort;
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
import com.swp.autocarwash.subscription.repository.FamilySubscriptionRepository;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    public CreateVehicleResponse createVehicle(
            Long userId,
            CreateVehicleRequest request
    ){

        Optional<Vehicle> existingVehicleOpt =
                vehicleRepository.findByLicensePlate(
                        request.getLicensePlate()
                );

        vehicleValidator
                .validateCreate(existingVehicleOpt);



        // Xác định customer đang đăng nhập dựa vào userId lấy từ JWT,
        // không còn nhận customerId trực tiếp từ FE (tránh client tự ý add xe cho người khác)
        Customer customer =
                customerPort.getCustomerReferenceByUserId(userId);



        Vehicle savedVehicle;

        if (existingVehicleOpt.isPresent()) {

            // Xe walk-in cũ chưa có chủ -> gắn customer hiện tại vào,
            // KHÔNG tạo dòng mới để tránh trùng license_plate và mất lịch sử cũ của xe
            Vehicle existingVehicle =
                    existingVehicleOpt.get();

            existingVehicle.setCustomer(customer);

            savedVehicle =
                    vehicleRepository.save(existingVehicle);

        } else {

            Vehicle vehicle =
                    Vehicle.builder()
                            .customer(customer)
                            .licensePlate(request.getLicensePlate())
                            .brandName(request.getBrandName())
                            .color(request.getColor())
                            // các field FE không gửi -> set giá trị mặc định
                            .violationCount(0)
                            .restrictedUntil(null)
                            .isDeleted(false)
                            .build();

            savedVehicle =
                    vehicleRepository.save(vehicle);

        }

        return vehicleMapper
                .toResponse(
                        savedVehicle
                );

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
        if(request.getLicensePlate() != null){
            boolean isPlateDup = vehicleRepository.existsByLicensePlateAndIdNotAndIsDeletedFalse(request.getLicensePlate(), vehicleId);
            if(isPlateDup){
                throw new BusinessException(ErrorCode.LICENSE_PLATE_ALREADY_EXISTS);
            }
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
