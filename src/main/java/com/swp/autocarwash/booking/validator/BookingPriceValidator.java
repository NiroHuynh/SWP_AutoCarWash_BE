package com.swp.autocarwash.booking.validator;


import com.swp.autocarwash.booking.dto.request.BookingPricePreviewRequest;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import org.springframework.stereotype.Component;

/**
 *
 * Chức năng: BookingPriceValidator dùng để kiểm tra tính hợp lệ của dữ liệu đầu vào
 * trước khi thực hiện quá trình tính toán giá booking.
 *
 * Class này đảm bảo request chứa đầy đủ thông tin bắt buộc để tránh lỗi
 * trong quá trình lấy dữ liệu service package, addon service và voucher.
 *
 * @author Phong
 * @version 1.0
 */
@Component
public class BookingPriceValidator {

    /**
     *
     * Chức năng: Kiểm tra dữ liệu request trước khi thực hiện tính toán giá booking.
     *
     * Quy trình:
     * - Nhận thông tin yêu cầu tính giá từ BookingPricePreviewRequest.
     * - Kiểm tra service package id có tồn tại hay không.
     * - Nếu dữ liệu không hợp lệ thì ném BusinessException.
     * - Nếu hợp lệ thì cho phép tiếp tục quá trình tính giá.
     *
     * @param request thông tin yêu cầu preview giá booking
     *
     * @return không trả về giá trị, kết thúc method khi dữ liệu hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    public void validate(BookingPricePreviewRequest request) {

        if (request.getServicePackageId() == null) {
            throw new BusinessException(ErrorCode.BOOKING_PRICE_CALCULATION_FAILED);
        }
    }
}
