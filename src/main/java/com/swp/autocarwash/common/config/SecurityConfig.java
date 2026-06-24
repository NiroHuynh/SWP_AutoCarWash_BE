package com.swp.autocarwash.common.config;

import com.swp.autocarwash.auth.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// author: Ngọc — thêm import cho CORS
import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;


//B2: Chuẩn bị nguyên liệu cho cuộc chơi bảo mật vòng trong
@Configuration  //dựng lên nhà xưởng chứa cấu hình
@EnableWebSecurity  //Điều động lập bốt gác vòng ngoài chặn URL Web
@EnableMethodSecurity   //Gác cổng ở vòng trong (tận các hàm xử lý code Service)
public class SecurityConfig {

    @Bean
    //ký gửi (@Bean) cái cỗ máy mã hoá mật khẩu này vào IOC Container để ví dụ như bên Login tiêm, chích nó vào sử dụng
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //Khai báo ông trùm quản lý xác thực trung tâm
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //Tiêm bộ lọc JWT được viết ở bước 3 vào đây
    //Hàm này nhào nặn ra cái bọc lính gác DefaultSecurityFilterChain(chứa 1 chuỗi các filter) -> để nạp vào ruột của FilterChainProxy
    //FilterChainProxy sẽ điều phối những url nào cần chạy qua SecurityFilterChain chẳng hạn
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
            //1. tắt CSRF
            http.csrf(AbstractHttpConfigurer::disable)
            // author: Ngọc — thêm cors() để Security không chặn preflight request từ FE
            .cors(Customizer.withDefaults())
            //2. chuyển sang STATELESS(ko lưu Session/Cookie trên Server)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            //3. quy định gác cổng, url nào được phép tự do, url nào phải khoá lại
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/error").permitAll()
                //mở toang cửa cho cụm API login
                //tất cả các API khác đều phải có token
                .anyRequest().authenticated()
            );
            //khi một request rơi vào ô này thì sẽ bị chặn lại -> thò tay vào SecurityContextHolder
            // check xem thử có Authentication không rồi mới cho qua/ nó check cái thẻ isAuthenticated()
            http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
    }

    // author: Ngọc — thêm bean CORS cho Security cho phép FE localhost:5173 gọi API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}