package com.swp.autocarwash.booking.service.impl;


import com.swp.autocarwash.booking.dto.request.BookingPricePreviewRequest;
import com.swp.autocarwash.booking.dto.response.BookingPricePreviewResponse;
import com.swp.autocarwash.booking.port.AddonServicePort;
import com.swp.autocarwash.booking.port.ServicePackagePort;
import com.swp.autocarwash.booking.port.VoucherPort;
import com.swp.autocarwash.booking.service.BookingPriceService;
import com.swp.autocarwash.booking.validator.BookingPriceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 *
 * Chức năng: BookingPriceServiceImpl triển khai nghiệp vụ tính toán giá booking
 * trước khi tạo booking. Class này xử lý việc tính giá service package, addon service,
 * kiểm tra voucher và tính tổng tiền cuối cùng.
 *
 * @author Phong
 * @version 1.0
 */

@Service
@RequiredArgsConstructor
public class BookingPriceServiceImpl implements BookingPriceService {

    private final ServicePackagePort servicePackagePort;
    private final AddonServicePort addonServicePort;
    private final VoucherPort voucherPort;
    private final BookingPriceValidator validator;

    /**
     *
     * Chức năng: Tính toán giá preview của booking dựa trên dịch vụ được chọn,
     * addon service và voucher áp dụng.
     *
     * Quy trình:
     * - Validate dữ liệu request trước khi tính giá.
     * - Lấy thông tin service package và lấy giá dịch vụ.
     * - Tính tổng giá addon service được chọn.
     * - Cộng service price và addon price để tạo subtotal.
     * - Kiểm tra voucher code có hợp lệ với giá trị đơn hàng hay không.
     * - Tính số tiền giảm giá nếu voucher hợp lệ.
     * - Tính tổng tiền cuối cùng sau khi giảm giá.
     * - Trả về BookingPricePreviewResponse chứa toàn bộ thông tin giá.
     *
     * @param request thông tin yêu cầu tính giá bao gồm service package,
     *                addon service và voucher code
     *
     * @return BookingPricePreviewResponse chứa chi tiết giá booking
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public BookingPricePreviewResponse calculatePreviewPrice(BookingPricePreviewRequest request) {

        validator.validate(request);

        // 1. Service package price
        var servicePackage = servicePackagePort.getServicePackage(request.getServicePackageId());
        BigDecimal servicePrice = servicePackage.getBasePrice();

        // 2. Addon price
        BigDecimal addonPrice = BigDecimal.ZERO;

        boolean haveAddons =  request.getAddonServiceIds()!=null && !request.getAddonServiceIds().isEmpty();

        if(haveAddons){
            addonPrice = addonServicePort.calculateAddonPrice(request.getAddonServiceIds());
        }

        BigDecimal subTotal = servicePrice.add(addonPrice);

        // 3. Voucher
        var voucherOpt = voucherPort.getVoucher(request.getVoucherCode(),subTotal);



        BigDecimal discount = BigDecimal.ZERO;
        boolean valid = false;
        Integer percent = null;

        if (voucherOpt.isPresent()) {
            var voucher = voucherOpt.get();

            valid = voucher.isValid(subTotal);
            if (valid) {
                percent = voucher.getDiscountPercentage();
                discount = subTotal.multiply(BigDecimal.valueOf(percent))
                        .divide(BigDecimal.valueOf(100));
            }
        }

        BigDecimal finalTotal = subTotal.subtract(discount);

        return BookingPricePreviewResponse.builder()
                .currency("VND")
                .breakdown(BookingPricePreviewResponse.PriceBreakdown.builder()
                        .servicePrice(servicePrice)
                        .addonPrice(addonPrice)
                        .subTotal(subTotal)
                        .voucherCode(request.getVoucherCode())
                        .voucherDiscount(discount)
                        .finalTotal(finalTotal)
                        .build())
                .appliedVoucher(BookingPricePreviewResponse.AppliedVoucher.builder()
                        .valid(valid)
                        .discountPercentage(percent)
                        .build())
                .build();
    }
}
