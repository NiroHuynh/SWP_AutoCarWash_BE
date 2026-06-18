package com.swp.autocarwash.auth.service.impl;

import com.nimbusds.jose.JOSEException;
import com.swp.autocarwash.auth.dto.request.LoginRequest;
import com.swp.autocarwash.auth.entity.User;
import com.swp.autocarwash.auth.entity.enums.IdentityType;
import com.swp.autocarwash.auth.exception.AccountDisabledException;
import com.swp.autocarwash.auth.repository.UserRepository;
import com.swp.autocarwash.auth.security.jwt.JwtProvider;
import com.swp.autocarwash.auth.validator.IdentityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepository userRepo;
    //dùng để mã hoá mật khẩu
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public String login(LoginRequest request) throws JOSEException {
        IdentityType identityType = IdentityValidator.detectType(request.getIdentity());
        User user = null;

        if(identityType == IdentityType.EMAIL){
            user = userRepo.findByEmailAndIsDeletedFalse(request.getIdentity());

        }else if(identityType == IdentityType.PHONE){
            user = userRepo.findByPhoneAndIsDeletedFalse(request.getIdentity());
        }

        System.out.println("=== [DEBUG LOG] User tìm thấy dưới DB: " + (user != null ? user.getEmail() : "NULL (Không tìm thấy!)"));

        if(user == null) {
            throw new BadCredentialsException("Tai khoan hoac mat khau khong chinh xac");
        }

        if(user.getIsActive() != null && !user.getIsActive()){
            throw new AccountDisabledException("Tai khoan da bi vo hieu hoa");
        }

        System.out.println("=== [DEBUG LOG] Mật khẩu thô Postman: [" + request.getPassword() + "]");
        System.out.println("=== [DEBUG LOG] Mật khẩu hash từ DB:  [" + user.getPasswordHash() + "]");

        boolean isPassWordMatch = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if(!isPassWordMatch){
            throw new BadCredentialsException("Tai khoan hoac mat khau khong chinh xac");
        }

        System.out.println("=== [DEBUG LOG] Kết quả BCrypt khớp nhau không?: " + isPassWordMatch);

        String actualToken = jwtProvider.generateToken(user);

        return actualToken;
    }
}
