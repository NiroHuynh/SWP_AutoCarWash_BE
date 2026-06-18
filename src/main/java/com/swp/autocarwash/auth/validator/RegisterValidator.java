package com.swp.autocarwash.auth.validator;

import com.swp.autocarwash.auth.dto.request.RegisterRequest;
import com.swp.autocarwash.auth.repository.UserRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

/**
 *
 * RegisterValidator là lớp dùng để kiểm tra tính hợp lệ của thông tin đăng ký người dùng.
 *
 * @author Phong
 * @version 1.0
 */

@Component
@RequiredArgsConstructor
public class RegisterValidator {

    private final UserRepository userRepository;
    private final PasswordValidator passwordValidator;

    /**
     * Validate register business rules
     *
     * @param request register request
     */
    public void validate(
            RegisterRequest request
    ){
        validateEmail(request.getEmail());
        validatePhone(request.getPhone());
        passwordValidator.validate(request.getPassword());
    }



    /**
     * Check email duplicate
     */
    private void validateEmail(
            String email
    ){

        if(userRepository.existsByEmail(email)){
            throw new BusinessException(
                    ErrorCode.EMAIL_ALREADY_EXISTS
            );

        }

    }



    /**
     * Check phone duplicate
     */
    private void validatePhone(
            String phone
    ){

        if(userRepository.existsByPhone(phone)){
            throw new BusinessException(
                    ErrorCode.PHONE_ALREADY_EXISTS
            );
        }

    }


}
