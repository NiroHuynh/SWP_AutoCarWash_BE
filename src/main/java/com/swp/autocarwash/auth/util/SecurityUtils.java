package com.swp.autocarwash.auth.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {


    public Long getCurrentUserId(){

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        System.out.println(auth.getName());
        return Long.valueOf(auth.getName());
    }

}
