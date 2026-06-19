package com.swp.autocarwash.booking.validator;


import com.swp.autocarwash.booking.dto.request.BookingPricePreviewRequest;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import org.springframework.stereotype.Component;

/**
 *
 * BookingPriceValidator dùng để validate dữ liệu đầu vào trước khi tính toán giá tiền
 *
 * @author Phong
 * @version 1.0
 */

@Component
public class BookingPriceValidator {

    /**
     * Validate input request before calculate price
     */
    public void validate(BookingPricePreviewRequest request) {

        if (request.getServicePackageId() == null) {
            throw new BusinessException(ErrorCode.BOOKING_PRICE_CALCULATION_FAILED);
        }

        // addon optional
    }
}
