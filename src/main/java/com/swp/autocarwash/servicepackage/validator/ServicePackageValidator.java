package com.swp.autocarwash.servicepackage.validator;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.servicepackage.repository.ServicePackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


/**
 *
 * Chức năng: ServicePackageValidator dùng để kiểm tra các business rule
 * liên quan đến dữ liệu service package.
 *
 * Class này đảm nhiệm việc validate dữ liệu đầu vào trước khi thực hiện
 * các nghiệp vụ lấy thông tin hoặc xử lý service package.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ServicePackageValidator {

    private final ServicePackageRepository servicePackageRepository;


    /**
     *
     * Chức năng: Kiểm tra service package id có hợp lệ hay không.
     *
     * Quy trình:
     * - Nhận service package id từ request hoặc module khác truyền vào.
     * - Kiểm tra id có null hay không.
     * - Kiểm tra id có phải là số dương hay không.
     * - Nếu dữ liệu không hợp lệ thì throw BusinessException.
     *
     * @param id service package id cần validate
     *
     * @return không trả về giá trị, throw exception nếu id không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    public void validateId(
            Integer id
    ){


        if(id == null || id <= 0){

            throw new BusinessException(
                    ErrorCode.INVALID_SERVICE_PACKAGE_ID
            );

        }

    }
    /**
     * Validate input khi CREATE service package
     */
    public void validateCreate(String name, BigDecimal basePrice, List<Integer> addonIds) {

        validateCommonFields(name, basePrice);
        validateAddonIds(addonIds);
        validateNameDuplicateForCreate(name);
    }


    /**
     * Validate input khi UPDATE service package
     */
    public void validateUpdate(Integer servicePackageId, String name, BigDecimal basePrice, List<Integer> addonIds) {

        validateCommonFields(name, basePrice);
        validateAddonIds(addonIds);
        validateNameDuplicateForUpdate(name, servicePackageId);
    }


    /**
     * Validate các field chung cho cả create và update
     */
    private void validateCommonFields(String name, BigDecimal basePrice) {

        // Name rỗng
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.SERVICE_PACKAGE_NAME_REQUIRED);
        }

        // Name bắt đầu bằng số
        if (Character.isDigit(name.trim().charAt(0))) {
            throw new BusinessException(ErrorCode.SERVICE_PACKAGE_NAME_INVALID);
        }

        // Price null hoặc <= 0
        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.SERVICE_PACKAGE_PRICE_INVALID);
        }
    }


    /**
     * Phải chọn ít nhất 1 addon
     */
    private void validateAddonIds(List<Integer> addonIds) {

        if (addonIds == null || addonIds.isEmpty()) {
            throw new BusinessException(ErrorCode.SERVICE_PACKAGE_ADDON_REQUIRED);
        }
    }


    /**
     * Check trùng name khi create
     */
    private void validateNameDuplicateForCreate(String name) {

        if (servicePackageRepository.existsByNameAndIsDeletedFalse(name.trim())) {
            throw new BusinessException(ErrorCode.SERVICE_PACKAGE_NAME_ALREADY_EXISTS);
        }
    }


    /**
     * Check trùng name khi update — loại trừ chính nó
     */
    private void validateNameDuplicateForUpdate(String name, Integer servicePackageId) {

        if (servicePackageRepository.existsByNameAndIsDeletedFalseAndIdNot(
                name.trim(), servicePackageId)) {
            throw new BusinessException(ErrorCode.SERVICE_PACKAGE_NAME_ALREADY_EXISTS);
        }
    }


}
