package com.swp.autocarwash.auth.security.principal;

import com.swp.autocarwash.auth.entity.User;
import com.swp.autocarwash.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service //BẮT BUỘC: Đánh dấu để Spring Boot tự động bốc class này bỏ vào kho Bean
public class UserCustomDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Chạy xuống DB dùng Repo để tìm User dựa vào Email công khai
        User user = userRepository.findByEmailAndIsDeletedFalse(email);

        // 2. Nếu không tìm thấy User nào, ném ngay lỗi của Spring Security ra để hệ thống chặn lại
        if (user == null) {
            throw new BadCredentialsException("User name or password incorrect");
        }

        // 3. ĐOẠN QUYẾT ĐỊNH: Bọc thực thể User của vào "Bản dịch" UserCustomDetails rồi trả về
        // Spring Security sẽ nhận lấy cục này và tự động so khớp mật khẩu
        return new UserCustomerDetails(user);
    }
}
