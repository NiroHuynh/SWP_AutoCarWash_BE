package com.swp.autocarwash.customer.service.vehicle.impl;

import com.swp.autocarwash.customer.dto.request.CreateVehicleRequest;
import com.swp.autocarwash.customer.dto.response.CreateVehicleResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class VehicleServiceImpl
        implements VehicleService {


    private final VehicleRepository vehicleRepository;

    private final VehicleValidator vehicleValidator;

    private final VehicleMapper vehicleMapper;

    private final CustomerPort customerPort;



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

}
