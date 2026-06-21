package com.swp.autocarwash.auth.exception;

import java.text.ParseException;

public class InvalidJwtException extends RuntimeException {

    public InvalidJwtException (String message){
        //Gọi constructor của cha và đưa lỗi cho cha gán hộ
        super(message);
    }
}
