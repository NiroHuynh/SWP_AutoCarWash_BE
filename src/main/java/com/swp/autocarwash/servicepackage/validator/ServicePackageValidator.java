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
     * Validate business rule cho create:
     * - Name không bắt đầu bằng số
     * - Check trùng name
     *
     * (Các check rỗng/price/addonIds đã do DTO annotation xử lý)
     */
    public void validateForCreate(String name) {

        validateNameFormat(name);
        validateNameDuplicateForCreate(name);
    }

    public void validateForUpdate(String name, Integer servicePackageId) {

        validateNameFormat(name);
        validateNameDuplicateForUpdate(name, servicePackageId);
    }


    /**
     * Name không được bắt đầu bằng số
     */
    private void validateNameFormat(String name) {

        if (name != null && !name.trim().isEmpty()
                && Character.isDigit(name.trim().charAt(0))) {
            throw new BusinessException(ErrorCode.SERVICE_PACKAGE_NAME_INVALID);
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
