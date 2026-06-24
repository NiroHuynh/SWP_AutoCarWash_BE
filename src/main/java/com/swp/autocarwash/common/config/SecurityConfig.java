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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config){
        return config.getAuthenticationManager();
    }

    //Tiêm bộ lọc JWT được viết ở bước 3 vào đây

    //Hàm này nhào nặn ra cái bọc lính gác DefaultSecurityFilterChain(chứa 1 chuỗi các filter) -> để nạp vào ruột của FilterChainProxy
    //FilterChainProxy sẽ điều phối những url nào cần chạy qua SecurityFilterChain chẳng hạn
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter){
            //1. tắt CSRF
            http.csrf(AbstractHttpConfigurer::disable)
            //2. chuyển sang STATELESS(ko lưu Session/Cookie trên Server)
            .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            //3. quy định gác cổng, url nào được phép tự do, url nào phải khoá lại
            .authorizeHttpRequests( auth -> auth.requestMatchers("/api/v1/auth/**").permitAll()
                            .requestMatchers("/error").permitAll()
            //mở toang cửa cho cụm API login
            .anyRequest().authenticated()
            );
            //tất cả các API khác đều phải có token
            // nghĩa là tại đây vào đều cần phải có xác thưc
            //khi một request rơi vào ô này thì sẽ bị chặn lại -> thò tay vào SecurityContextHolder
            // check xem thử có Authentication không rồi mới cho qua/ nó check cái thẻ isAuthenticated()
            http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
    }


}
