package com.swp.autocarwash.auth.security.filter;

import com.swp.autocarwash.auth.entity.User;
import com.swp.autocarwash.auth.repository.UserRepository;
import com.swp.autocarwash.auth.security.jwt.JwtProvider;
import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1. Trích xuất header mang tên "Authorization" từ request lên
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        //Nếu header trống hoặc không bắt đầu bằng chữ Bearer
        //-> bỏ qua, cho đi tiếp sang bộ lọc sau (khách vãng lai) tại đây chưa có đuổi nó -> vì nó đang được xếp đầu trong chuỗi
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        //Cắt 7 kí tự đầu Bearer để lấy đúng chuỗi token nguyên bản
        jwtToken = authHeader.substring(7);

        //Giải mã token -  lấy ra cái định danh Email
        userEmail = jwtProvider.extractEmail(jwtToken);
        //nếu có email và chưa có ai nạp vào authentication
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // Tìm User dưới DB lên dựa vào Email đã giải mã từ Token
            User user = userRepository.findByEmailAndIsDeletedFalse(userEmail);
            if(user != null && jwtProvider.isTokenValid(jwtToken,user)){
                //kiểm tra token có hết hạn không

                // Dịch sang cấu trúc UserDetails của Spring Security
                UserCustomerDetails userDetails = new UserCustomerDetails(user);

                //TẠO TẤM THẺ BÀI THÔNG QUAN (Authentication): Đóng gói User + Danh sách Quyền (Role) lại
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                //TIÊM DANH TÍNH VÀO HỆ THỐNG: Bỏ tấm thẻ bài vào túi áo của Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("=== [FILTER LOG] Xác thực thành công cho user: " + userEmail + " với Role: " + userDetails.getAuthorities());
            }
            filterChain.doFilter(request, response);
        }

    }
}
