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
import com.swp.autocarwash.auth.validator.RegisterValidator;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.loyalty.entity.LoyaltyPointBalance;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointBalanceRepository;
import com.swp.autocarwash.staff.entity.Staff;
import com.swp.autocarwash.staff.repository.custom.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.swp.autocarwash.auth.dto.request.RegisterRequest;
import com.swp.autocarwash.auth.entity.Role;
import com.swp.autocarwash.auth.entity.enums.UserRole;
import com.swp.autocarwash.auth.repository.RoleRepository;
import com.swp.autocarwash.auth.service.AuthService;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.loyalty.entity.CustomerTier;
import com.swp.autocarwash.loyalty.entity.enums.TierStatus;
import com.swp.autocarwash.loyalty.repository.CustomerTierRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


/**
 *
 * AuthServiceImpl là  lớp triển khai của AuthService, cung cấp các chức năng liên quan đến xác thực người dùng.
 *
 * @author Phong
 * @version 1.0
 */

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    private final RegisterValidator registerValidator;

    private final RoleRepository roleRepository;

    private final CustomerRepository customerRepository;

    private final CustomerTierRepository customerTierRepository;
          // ← thêm field này
    private final LoyaltyPointBalanceRepository loyaltyPointBalanceRepository;

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
    private StaffRepository staffRepository;


    /**
     * Register new user account
     *
     * Flow:
     * 1. Validate register request
     * 2. Create user
     * 3. Save user
     * 4. Send request to customer module
     * 5. Return response
     */
    @Override
    @Transactional
    public boolean register(RegisterRequest request) {

        registerValidator.validate(request);

        Role customerRole =
                roleRepository.findByName(UserRole.CUSTOMER.name())
                        .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_FOUND));

        CustomerTier memberTier =
                customerTierRepository.findByTierName(
                        TierStatus.MEMBER.name()
                ).orElseThrow(() -> new BusinessException(ErrorCode.TIER_NOT_FOUND));

        User user = User.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(customerRole)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        Customer customer = new Customer();

        customer.setUser(user);
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setBirthday(request.getBirthday());

        customer.setCustomerTier(memberTier);
        customer.setViolationCount(0);
        customer.setRestrictedUntil(null);

        Customer savedCustomer =
                customerRepository.save(customer);

        LoyaltyPointBalance loyaltyPointBalance = new LoyaltyPointBalance();
        loyaltyPointBalance.setCustomer(savedCustomer);
        loyaltyPointBalance.setTotalPoints(0);
        loyaltyPointBalance.setAccumulatedPoints(0);

        loyaltyPointBalanceRepository.save(loyaltyPointBalance);

        return true;
      }


   

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
        Integer finalStationId = null;

        if ("ADMIN".equals(roleName)) {
            displayName = "ADMIN";
        } else if ("CUSTOMER".equals(roleName)) {

            Customer customer = customerRepository.findByUserId(user.getId());


            if (customer != null) {
                displayName = customer.getLastName() + " " +customer.getFirstName();          // Nhặt name từ bảng customers
            } else {

                throw new RuntimeException("Customer profile not found");
            }
        }else if("STAFF".equals(roleName)){
            Staff staff = staffRepository.findByUserId(user.getId());
            if (staff != null) {
                displayName = staff.getLastName() + " " + staff.getFirstName();
                if (staff.getStation() != null) {
                    finalStationId = staff.getStation().getId();
                }
            } else {

            throw new RuntimeException("Staff profile not found");
        }
        }
        String token = jwtProvider.generateToken(user);
        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(displayName)
                .stationId(finalStationId).build();
    }

}
