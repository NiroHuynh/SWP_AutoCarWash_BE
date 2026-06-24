package com.swp.autocarwash.auth.service.impl;

import com.swp.autocarwash.auth.dto.request.RegisterRequest;
import com.swp.autocarwash.auth.dto.response.RegisterResponse;
import com.swp.autocarwash.auth.entity.Role;
import com.swp.autocarwash.auth.entity.User;
import com.swp.autocarwash.auth.entity.enums.UserRole;
import com.swp.autocarwash.auth.mapper.UserMapper;
import com.swp.autocarwash.auth.port.CustomerPort;
import com.swp.autocarwash.auth.repository.RoleRepository;
import com.swp.autocarwash.auth.repository.UserRepository;
import com.swp.autocarwash.auth.service.AuthService;
import com.swp.autocarwash.auth.validator.RegisterValidator;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.custom.CustomerRepository;
import com.swp.autocarwash.loyalty.entity.CustomerTier;
import com.swp.autocarwash.loyalty.entity.enums.TierStatus;
import com.swp.autocarwash.loyalty.repository.custom.CustomerTierRepository;
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

    private final RoleRepository roleRepository;

    private final CustomerRepository customerRepository;

    private final CustomerTierRepository customerTierRepository;



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
    public boolean register(RegisterRequest request) {
        Role customerRole =
                roleRepository.findByName(UserRole.CUSTOMER.name())
                        .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_FOUND));

        CustomerTier memberTier =
                customerTierRepository.findByTierName(
                        TierStatus.MEMBER.name()
                ).orElseThrow(() -> new BusinessException(ErrorCode.TIER_NOT_FOUND));

        User user = User.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(customerRole)
                .build();

        user = userRepository.save(user);

        Customer customer = new Customer();

        customer.setUser(user);
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setBirthday(request.getBirthday());

        customer.setCustomerTier(memberTier);
        customer.setViolationCount(0);
        customer.setRestrictedUntil(null);

        customerRepository.save(customer);

        return true;
    }

}
