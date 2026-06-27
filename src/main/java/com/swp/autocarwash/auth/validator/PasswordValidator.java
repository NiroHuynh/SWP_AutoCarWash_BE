package com.swp.autocarwash.auth.validator;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import org.springframework.stereotype.Component;

/**
 *
 * PasswordValidator là lớp dùng để kiểm tra tính hợp lệ của mật khẩu người dùng.
 *
 * @author Phong
 * @version 1.0
 */

@Component
public class PasswordValidator {



    /**
     * Validate password business rule
     *
     * @param password raw password
     */
    public void validate(
            String password
    ){

        if(password == null || password.length() < 6
        ){
            throw new BusinessException(
                    ErrorCode.INVALID_PASSWORD
            );
        }

    }

}
