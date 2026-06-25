package com.swp.autocarwash.auth.service;

import com.swp.autocarwash.auth.dto.request.RegisterRequest;
import com.swp.autocarwash.auth.dto.response.RegisterResponse;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {


    /**
     * Register new user account
     *
     * @param request register information
     * @return true if registration is successful, false otherwise
     */
    boolean register(
            RegisterRequest request
    );

}
