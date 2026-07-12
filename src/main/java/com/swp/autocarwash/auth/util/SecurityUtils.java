package com.swp.autocarwash.auth.util;

import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.UnauthorizedException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final CustomerRepository customerRepository;

    /**
     * Lấy userId của user hiện tại từ SecurityContext.
     *
     * Flow:
     * - Lấy Authentication hiện tại.
     * - Kiểm tra authentication có tồn tại và đã authenticated.
     * - Lấy principal name (sub trong JWT).
     * - Convert sang Long userId.
     *
     * @return userId của user đang đăng nhập
     */
    public Long getCurrentUserId(){

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserCustomerDetails userCustomerDetails)) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        // Lấy đúng id số của User entity (đã có sẵn getUser() trong UserCustomerDetails),
        // không dùng auth.getName() vì nó luôn trả về email (do getUsername() override trả email)
        return userCustomerDetails.getUser().getId();
    }

    public Customer getCustomer(){
        Long userId = getCurrentUserId();
        return customerRepository.findByUserId(userId);
    }
}
