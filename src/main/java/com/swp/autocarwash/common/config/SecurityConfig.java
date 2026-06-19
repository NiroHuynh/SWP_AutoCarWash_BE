package com.swp.autocarwash.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Cấu hình Spring Security tạm thời dùng trong giai đoạn phát triển.
 *
 * <p><b>Lưu ý:</b> Class này cho phép tất cả request mà không cần xác thực.
 * Khi module auth hoàn thiện, dev phụ trách auth sẽ thay thế class này
 * bằng cấu hình JWT filter đầy đủ.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Cấu hình SecurityFilterChain tắt CSRF và cho phép tất cả request không cần xác thực.
     *
     * @param http đối tượng {@link HttpSecurity} do Spring inject
     * @return {@link SecurityFilterChain} đã được cấu hình
     * @throws Exception nếu quá trình build filter chain thất bại
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
