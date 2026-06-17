package com.swp.autocarwash.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
     public BCryptPasswordEncoder passwordEncoder(){
         return new BCryptPasswordEncoder();
     }
}
