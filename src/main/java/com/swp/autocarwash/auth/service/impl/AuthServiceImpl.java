package com.swp.autocarwash.auth.service.impl;

import com.swp.autocarwash.auth.dto.request.RegisterRequest;
import com.swp.autocarwash.auth.dto.response.RegisterResponse;
import com.swp.autocarwash.auth.entity.User;
import com.swp.autocarwash.auth.mapper.UserMapper;
import com.swp.autocarwash.auth.port.CustomerPort;
import com.swp.autocarwash.auth.repository.UserRepository;
import com.swp.autocarwash.auth.service.AuthService;
import com.swp.autocarwash.auth.validator.RegisterValidator;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 * AuthServiceImpl là  lớp triển khai của AuthService, cung cấp các chức năng liên quan đến xác thực người dùng.
 *
 * @author Phong
 * @version 1.0
 */

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RegisterValidator registerValidator;

    private final PasswordEncoder passwordEncoder;

    private final CustomerPort customerPort;



    /**
     * Register new user account
     *
     * Flow:
     * 1. Validate register request
     * 2. Create user
     * 3. Save user
     * 4. Send request to customer module
     * 5. Return response
     */
    @Override
    @Transactional
    public boolean register(
            RegisterRequest request
    ) {
        registerValidator.validate(request);

        User user =
                User.builder()
                        .email(request.getEmail())
                        .phone(request.getPhone())
                        .passwordHash(passwordEncoder.encode(request.getPassword()))
                        .build();

        User savedUser =
                userRepository.save(user);

        CustomerContract customerContract =
                CustomerContract.builder()
                        .userId(Integer.parseInt(savedUser.getId().toString()))
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .birthday(request.getBirthday())
                        .build();

        customerPort.createCustomer(
                customerContract
        );

        return true;

    }

}
