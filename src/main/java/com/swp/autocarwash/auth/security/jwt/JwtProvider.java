package com.swp.autocarwash.auth.security.jwt;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.swp.autocarwash.auth.entity.User;
import com.swp.autocarwash.auth.exception.InvalidJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    public String generateToken(User user) throws JOSEException{
        //tao jws header(khai bao thuat toan ma hoa)
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        //Tao jwt claimset/ nam trong payload
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        //lấy customer id từ user entity
        Long customerId = (user.getCustomer() != null) ? user.getCustomer().getId() : null;

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                                    .subject(user.getId().toString())
                                    .issueTime(now)
                                    .expirationTime(expiryDate)
                                    .claim("roles", user.getRole().getName())
                                    .claim("email", user.getEmail())
                                    .claim("customerId", customerId)    //đút id vào token
                                    .build();
        //chuyen claim set thanh payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        JWSSigner signer = new MACSigner(jwtSecret.getBytes());

        //ky so
        //tao ra chuoi token dua tren thuan toan hash
        jwsObject.sign(signer);

        return jwsObject.serialize();
    }

    public String extractEmail(String token)  {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimSet = signedJWT.getJWTClaimsSet();
            return claimSet.getStringClaim("email");
        }
        catch(Exception e){
            throw new InvalidJwtException("Invalid JWT token");
        }
    }

    //HÀM KIỂM TRA
    public boolean isTokenValid(String token, User user) {
        // BƯỚC 1: Kiểm tra xem chữ ký của Token có hợp lệ và trùng khớp với SecretKey của hệ thống không
        if (!verifySignature(token)) {
            System.out.println("=== [SECURITY WARNING] Token có chữ ký KHÔNG hợp lệ!");
            return false;
        }

        // BƯỚC 2: Kiểm tra logic thời hạn và danh tính
        final String email = extractEmail(token);
        return (email != null && email.equals(user.getEmail()) && !isTokenExpired(token));
    }

    // 2. HÀM KIỂM TRA CHỮ KÝ THỦ CÔNG (Nimbus JOSE)
    private boolean verifySignature(String token) {
        try {
            // Ép chuỗi Token String về đúng định dạng mã hóa SignedJWT
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Tạo ra một bộ giải mã sử dụng chính cái Secret Key bạn cấu hình trong file properties
            JWSVerifier verifier = new MACVerifier(jwtSecret.getBytes());

            // Thực hiện thuật toán so khớp chữ ký đồ họa và trả về kết quả true/false
            return signedJWT.verify(verifier);
        } catch (Exception e) {
            // Nếu có bất kỳ lỗi gì xảy ra (Token rác, sai định dạng chữ ký), coi như không hợp lệ
            return false;
        }
    }

    // 4. Hàm kiểm tra hết hạn (Giữ nguyên)
    private boolean isTokenExpired(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return claims.getExpirationTime().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
