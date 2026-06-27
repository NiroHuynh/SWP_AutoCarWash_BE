package com.swp.autocarwash.auth.controller;
import com.nimbusds.jose.JOSEException;
import com.swp.autocarwash.auth.dto.request.LoginRequest;
import com.swp.autocarwash.auth.dto.response.LoginResponse;
import com.swp.autocarwash.auth.service.AuthService;
import com.swp.autocarwash.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.swp.autocarwash.auth.dto.request.RegisterRequest;
import com.swp.autocarwash.auth.dto.response.RegisterResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) throws JOSEException {
        //gọi service xử lí và lấy token về
        LoginResponse loginResponse = authService.login(request);
        ApiResponse<LoginResponse> response = ApiResponse.success("Login successfully", loginResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

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
