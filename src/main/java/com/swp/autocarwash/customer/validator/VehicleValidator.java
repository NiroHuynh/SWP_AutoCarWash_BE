package com.swp.autocarwash.customer.validator;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validator xử lý các rule nghiệp vụ liên quan Vehicle.
 *
 * Quy tắc check biển số trùng:
 * - is_deleted = false → xe đang hoạt động → chặn
 * - is_deleted = true  → xe đã bị xóa mềm → cho phép tạo/update sang biển đó
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class VehicleValidator {

    private final VehicleRepository vehicleRepository;

    /**
     * Kiểm tra biển số trước khi tạo xe mới.
     *
     * Chỉ báo lỗi khi đã tồn tại xe CHƯA BỊ XÓA (is_deleted = false) với cùng biển số.
     * Nếu xe cùng biển số đã bị xóa mềm → cho phép tạo mới.
     *
     * @param licensePlate biển số đã được normalize (trim + uppercase)
     */
    public void validateCreate(String licensePlate) {
        boolean plateInUse =
                vehicleRepository.existsActiveVehicleByLicensePlate(licensePlate);

        if (plateInUse) {
            throw new BusinessException(ErrorCode.LICENSE_PLATE_ALREADY_EXISTS);
        }
    }

    /**
     * Kiểm tra biển số trước khi cập nhật xe.
     *
     * Chỉ báo lỗi khi tồn tại xe KHÁC (id != vehicleId) CHƯA BỊ XÓA với cùng biển số.
     * Nếu xe cùng biển số đã bị xóa mềm → cho phép update sang biển đó.
     *
     * @param licensePlate biển số mới muốn đổi sang (đã normalize)
     * @param vehicleId    id của xe đang được cập nhật (để loại trừ chính nó)
     */
    public void validateUpdate(String licensePlate, Long vehicleId) {
        boolean plateInUse =
                vehicleRepository.existsByLicensePlateAndIdNotAndIsDeletedFalse(
                        licensePlate, vehicleId
                );

        if (plateInUse) {
            throw new BusinessException(ErrorCode.LICENSE_PLATE_ALREADY_EXISTS);
        }
    }
}