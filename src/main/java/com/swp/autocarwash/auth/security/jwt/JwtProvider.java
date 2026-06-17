package com.swp.autocarwash.auth.security.jwt;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.swp.autocarwash.auth.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                                    .subject(user.getId().toString())
                                    .issueTime(now)
                                    .expirationTime(expiryDate)
                                    .claim("roles", user.getRole())
                                    .claim("email", user.getEmail())
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
}
