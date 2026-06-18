package com.swp.autocarwash.auth.controller;

import com.swp.autocarwash.auth.dto.request.RegisterRequest;
import com.swp.autocarwash.auth.dto.response.RegisterResponse;
import com.swp.autocarwash.auth.service.AuthService;
import com.swp.autocarwash.common.response.ApiResponse;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    /**
     * Register new user account
     *
     * @param request register information
     * @return register result
     */
    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(
            @Valid
            @RequestBody RegisterRequest request
    ) {

        authService.register(request);

        return ApiResponse.success(
                "Register successfully",
                null
        );

    }

}
