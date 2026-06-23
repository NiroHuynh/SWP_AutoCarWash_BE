package com.swp.autocarwash.common.exception;


import com.swp.autocarwash.common.exception.code.ErrorCode;

//exception dùng cho các lỗi vi phạm quy tắc nghiệp vụ (không phải lỗi hệ thống)
//vd:Khi khách đến sớm và cửa hàng không còn làn trống
public class BusinessException
        extends BaseException {


    public BusinessException(
            ErrorCode errorCode
    ){

        super(errorCode);
    }

}
