package com.swp.autocarwash.servicepackage.validator;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import org.springframework.stereotype.Component;


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
public class ServicePackageValidator {



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


}
