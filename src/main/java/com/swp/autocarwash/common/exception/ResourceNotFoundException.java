package com.swp.autocarwash.common.exception;

import com.swp.autocarwash.common.exception.code.ErrorCode;

public class ResourceNotFoundException
        extends BaseException {


    public ResourceNotFoundException(
            ErrorCode errorCode
    ){

        super(errorCode);
    }
}