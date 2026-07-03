package com.swp.autocarwash.auth.service.impl;

import com.swp.autocarwash.auth.dto.request.ChangePasswordRequest;
import com.swp.autocarwash.auth.entity.User;
import com.swp.autocarwash.auth.repository.UserRepository;
import com.swp.autocarwash.auth.service.PasswordService;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.UnauthorizedException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(ChangePasswordRequest request) {
        // 1. Bốc username/email của user hiện tại từ Token thông qua SecurityContext
        // 2. Chốt chặn an toàn: Nếu chưa đăng nhập hoặc token lỗi thì ném lỗi ngay
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
        }
        String currentUser = authentication.getName();
        User user = userRepository.findByEmailAndIsDeletedFalse(currentUser);
        if(user == null ){
            throw new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND);
        }
        // Kiểm tra xem mật khẩu hiện tại nhập vào có đúng không
        // Vì mật khẩu trong DB đã bị hash bằng BCrypt nên bắt buộc phải dùng hàm .matches()
        if(!passwordEncoder.matches(request.getCurrentPassword(),user.getPasswordHash())){
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
        //Kiểm tra xem mật khẩu mới có bị trùng mật khẩu cũ hay không
        if(passwordEncoder.matches(request.getNewPassword(),user.getPasswordHash())){
            throw new BusinessException(ErrorCode.PASSWORD_SAME_AS_CURRENT);
        }
        //Kiểm tra xem mật khẩu xác nhận có khớp mật khẩu mới không
        if(!Objects.equals(request.getNewPassword(), request.getConfirmNewPassword())){
            throw new BusinessException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }

        String hashNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPasswordHash(hashNewPassword);
        userRepository.save(user);
    }
}
