package com.swp.autocarwash.auth.service.impl;

import com.nimbusds.jose.JOSEException;
import com.swp.autocarwash.auth.dto.request.LoginRequest;
import com.swp.autocarwash.auth.dto.response.LoginResponse;
import com.swp.autocarwash.auth.entity.User;
import com.swp.autocarwash.auth.entity.enums.IdentityType;
import com.swp.autocarwash.auth.exception.AccountDisabledException;
import com.swp.autocarwash.auth.repository.UserRepository;
import com.swp.autocarwash.auth.security.jwt.JwtProvider;
import com.swp.autocarwash.auth.validator.IdentityValidator;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.staff.entity.Staff;
import com.swp.autocarwash.staff.repository.custom.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepository userRepo;
    //dùng để mã hoá mật khẩu
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AuthenticationManager authenManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public LoginResponse login(LoginRequest request) throws JOSEException {
        //nhận diện tk xem thử là email hay là phone
        IdentityType identityType = IdentityValidator.detectType(request.getIdentity());
        String finalEmail = request.getIdentity();

        //Nếu user đăng nhập bằng sdt thì đổi thành gmail để đồng bộ với cấu hình security
        if (identityType == IdentityType.PHONE) {
            User userByPhone = userRepo.findByPhoneAndIsDeletedFalse(request.getIdentity());
            if (userByPhone == null) {
                throw new BadCredentialsException("User name or password incorrect");
            }
            finalEmail = userByPhone.getEmail();
        }

        try {
            // PHÁT LỆNH XÁC THỰC: Đưa Email và Mật khẩu thô cho Spring Security tự lo liệu
            // Hậu trường: Nó sẽ tự bốc mật khẩu thô so khớp với mật khẩu hash dưới DB, tự check xem có bị disable/delete không
            authenManager.authenticate(
                    new UsernamePasswordAuthenticationToken(finalEmail, request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("User name or password incorrect");
        } catch (DisabledException e) {
            throw new AccountDisabledException("Account is disabled");
        }

        // 2. Vượt qua cửa ải xác thực trên an toàn -> Tìm User lên để ký sinh nổ ra Token JWT trả về cho khách
        User user = userRepo.findByEmailAndIsDeletedFalse(finalEmail);

        // 3. Khởi tạo biến hứng tên và lấy Tên Role ra check
        String displayName = "User System"; // Tên mặc định đề phòng
        String roleName = user.getRole().getName(); // Lấy chuỗi "CUSTOMER", "STAFF", "ADMIN"

        if ("ADMIN".equals(roleName)) {
            displayName = "ADMIN";
        } else if ("CUSTOMER".equals(roleName)) {

            Customer customer = customerRepository.findByUserId(user.getId());

            // Kiểm tra xem bên trong cái hộp Optional có dữ liệu hay không
            if (customer != null) {
                displayName = customer.getLastName() + " " +customer.getFirstName();          // Nhặt name từ bảng customers
            } else {
                // Nếu hộp trống rỗng thì chủ động ném lỗi
                throw new RuntimeException("Customer profile not found");
            }
        }else if("STAFF".equals(roleName)){
            Staff staff = staffRepository.findByUserId(user.getId());
            if (staff != null) {
                displayName = staff.getLastName() + " " + staff.getFirstName();
            } else {
            // Nếu hộp trống rỗng thì chủ động ném lỗi
            throw new RuntimeException("Staff profile not found");
        }
        }
        String token = jwtProvider.generateToken(user);
        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(displayName).build();
    }

}
