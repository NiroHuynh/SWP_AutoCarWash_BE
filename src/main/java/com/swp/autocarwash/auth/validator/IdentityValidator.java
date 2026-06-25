package com.swp.autocarwash.auth.validator;

import com.swp.autocarwash.auth.entity.enums.IdentityType;

import java.util.regex.Pattern;

public class IdentityValidator {

    //Regex check phone valid
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{9,11}$");

    //Phân tích chuỗi đầu vào
    public static IdentityType detectType(String identity){
        if(identity == null || identity.isBlank()){
            return IdentityType.INVALID;
        }
        //contain @ -> email
        if(identity.contains("@")){
            return IdentityType.EMAIL;
        }

        // valid phone
        if(PHONE_PATTERN.matcher(identity).matches()){
            return IdentityType.PHONE;
        }
        //nhập lung tung, không đúng định dạng cả 2
        return IdentityType.INVALID;
    }


}
