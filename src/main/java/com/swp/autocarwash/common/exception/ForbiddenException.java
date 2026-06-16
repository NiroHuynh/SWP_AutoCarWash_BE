package com.swp.autocarwash.common.exception;


import com.swp.autocarwash.common.exception.code.ErrorCode;

public class ForbiddenException
        extends BaseException {


    public ForbiddenException(
            ErrorCode errorCode
    ){

        super(errorCode);
    }
}
