package com.swp.autocarwash.common.exception;

import com.swp.autocarwash.common.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class CustomerRestrictedException extends BaseException {

    private final long remainingDays;

    public CustomerRestrictedException(long remainingDays) {
        super(ErrorCode.CUSTOMER_RESTRICTED);
        this.remainingDays = remainingDays;
    }
}
