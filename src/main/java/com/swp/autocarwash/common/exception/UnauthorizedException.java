package com.swp.autocarwash.common.exception;

import com.swp.autocarwash.common.exception.code.ErrorCode;

public class UnauthorizedException
        extends BaseException {


    public UnauthorizedException(
            ErrorCode errorCode
    ){

        super(errorCode);
    }
}
