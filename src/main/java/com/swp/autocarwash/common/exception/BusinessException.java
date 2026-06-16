package com.swp.autocarwash.common.exception;


import com.swp.autocarwash.common.exception.code.ErrorCode;

public class BusinessException
        extends BaseException {


    public BusinessException(
            ErrorCode errorCode
    ){

        super(errorCode);
    }
}
