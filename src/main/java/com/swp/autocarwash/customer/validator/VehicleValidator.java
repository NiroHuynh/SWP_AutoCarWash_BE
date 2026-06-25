package com.swp.autocarwash.customer.validator;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.dto.request.CreateVehicleRequest;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 *
 * Validator xử lý các rule nghiệp vụ liên quan Vehicle
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class VehicleValidator {


    private final VehicleRepository vehicleRepository;



    /**
     *
     * Kiểm tra dữ liệu trước khi tạo vehicle.
     *
     * Biển số trùng chỉ bị chặn khi xe đó ĐÃ CÓ customer (đã có chủ thật).
     * Nếu xe tồn tại nhưng customer đang null (ví dụ xe walk-in cũ được tạo
     * lúc check-in, chưa gắn tài khoản nào) -> cho qua, service sẽ gắn
     * customer hiện tại vào chính xe đó thay vì tạo dòng mới.
     *
     * @param existingVehicle xe tìm được theo biển số (nếu có), do service truyền vào
     */
    public void validateCreate(
            Optional<Vehicle> existingVehicle
    ) {

        boolean licensePlateAlreadyExists =
                existingVehicle.isPresent()
                        && existingVehicle.get().getCustomer() != null;

        if (licensePlateAlreadyExists) {

            throw new BusinessException(
                    ErrorCode.LICENSE_PLATE_ALREADY_EXISTS
            );

        }

    }


}