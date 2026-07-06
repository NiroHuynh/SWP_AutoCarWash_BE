package com.swp.autocarwash.customer.controller;

import com.swp.autocarwash.auth.dto.request.UpdateProfileRequest;
import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.customer.dto.response.CustomerProfileResponse;
import com.swp.autocarwash.customer.dto.response.CustomerUpdateProfileResponse;
import com.swp.autocarwash.customer.service.customer.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<CustomerProfileResponse>> getProfile(@AuthenticationPrincipal UserCustomerDetails userDetails) {
        //Bốc thẳng ID từ hộp lưu giữ trên RAM
        Long loggedInCustomerId = userDetails.getCustomerId();

        CustomerProfileResponse response = customerService.getCustomerProfile(loggedInCustomerId);
        return ResponseEntity.ok(ApiResponse.success(response.getMessage(), response));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<CustomerUpdateProfileResponse>> updateProfile(
            @AuthenticationPrincipal UserCustomerDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request) {

        // 1. Lấy customerId an toàn từ Token đã giải mã ở Filter
        Long customerId = userDetails.getCustomerId();
        // 2. Gọi xuống Service xử lý
        CustomerUpdateProfileResponse response = customerService.updateCustomerProfile(customerId, request);
        // 3. Trả về thông báo thành công
        return ResponseEntity.ok(ApiResponse.success("Your information has been updated successfully.",response));
    }
}
