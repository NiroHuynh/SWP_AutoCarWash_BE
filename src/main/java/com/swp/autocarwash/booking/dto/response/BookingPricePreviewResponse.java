package com.swp.autocarwash.booking.dto.response;



import lombok.*;
import java.math.BigDecimal;

/**
 *
 * Chức năng: BookingPricePreviewResponse dùng để chứa kết quả xem trước giá booking
 * trước khi khách hàng thực hiện tạo booking. Response bao gồm loại tiền tệ,
 * chi tiết giá, thông tin giảm giá và voucher được áp dụng.
 *
 * @author Phong
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingPricePreviewResponse {

    private String currency;

    private PriceBreakdown breakdown;

    private AppliedVoucher appliedVoucher;

    /**
     *
     * Chức năng: PriceBreakdown lưu trữ chi tiết quá trình tính toán giá booking,
     * bao gồm giá dịch vụ, giá addon, tổng tiền trước giảm giá, giảm giá voucher
     * và tổng tiền cuối cùng cần thanh toán.
     *
     * @author Phong
     * @version 1.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PriceBreakdown {
        private BigDecimal servicePrice;
        private BigDecimal addonPrice;
        private BigDecimal subTotal;

        private String voucherCode;
        private BigDecimal voucherDiscount;

        private BigDecimal finalTotal;
    }

    /**
     *
     * Chức năng: AppliedVoucher lưu trữ thông tin voucher được áp dụng vào booking,
     * bao gồm trạng thái hợp lệ và phần trăm giảm giá của voucher.
     *
     * @author Phong
     * @version 1.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AppliedVoucher {
        private boolean valid;
        private Integer discountPercentage;
    }
}
