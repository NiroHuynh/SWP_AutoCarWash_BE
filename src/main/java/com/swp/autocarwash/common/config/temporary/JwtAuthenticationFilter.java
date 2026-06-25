package com.swp.autocarwash.common.config.temporary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {


        String header = request.getHeader("Authorization");


        if (header != null && header.startsWith("Bearer ")) {


            String token = header.substring(7);


            // JWT = header.payload.signature
            String payload = token.split("\\.")[1];


            String json =
                    new String(
                            Base64.getUrlDecoder()
                                    .decode(payload),
                            StandardCharsets.UTF_8
                    );


            ObjectMapper mapper = new ObjectMapper();


            JsonNode node =
                    mapper.readTree(json);


            String userId =
                    node.get("sub").asText();


            String email =
                    node.get("email").asText();


            String role =
                    node.get("roles").asText();


            System.out.println("USER ID = " + userId);
            System.out.println("EMAIL = " + email);
            System.out.println("ROLE = " + role);



            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            List.of()
                    );


            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
        }


        filterChain.doFilter(request, response);
    }
}