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
 * BookingPriceServiceImpl dùng để tính toán giá tiền tạm thời dựa trên các dịch vụ đã chọn
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

    @Override
    public BookingPricePreviewResponse calculatePreviewPrice(BookingPricePreviewRequest request) {

        validator.validate(request);

        // 1. Service package price
        var servicePackage = servicePackagePort.getServicePackage(request.getServicePackageId());
        BigDecimal servicePrice = servicePackage.getBasePrice();

        // 2. Addon price
        BigDecimal addonPrice = addonServicePort.calculateAddonPrice(request.getAddonServiceIds());

        BigDecimal subTotal = servicePrice.add(addonPrice);

        // 3. Voucher
        var voucherOpt = voucherPort.getVoucher(request.getVoucherCode());

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
