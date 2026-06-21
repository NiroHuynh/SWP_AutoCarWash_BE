package com.swp.autocarwash.booking.calculator;

import java.math.BigDecimal;
import java.util.List;

import com.swp.autocarwash.booking.port.AddonServicePort;
import com.swp.autocarwash.booking.port.VoucherPort;
import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 *
 * Chức năng: BookingPriceCalculator đảm nhiệm việc tính toán giá tiền của một booking dựa trên
 * service package, addon services và voucher code. Class này xử lý toàn bộ logic tính giá
 * trước khi tạo booking.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class BookingPriceCalculator {

    private final AddonServicePort addonServicePort;
    private final VoucherPort voucherPort;

    /**
     *
     * Chức năng: BookingPriceCalculator đảm nhiệm việc tính toán giá tiền của một booking dựa trên
     * service package, addon services và voucher code. Class này xử lý toàn bộ logic tính giá
     * trước khi tạo booking.
     *
     * @author Phong
     * @version 1.0
     */
    public PriceResult calculate(
            ServicePackageContract servicePackage,
            List<Integer> addonIds,
            String voucherCode
    ) {

        BigDecimal servicePrice = servicePackage.getBasePrice();

        BigDecimal addonPrice = addonServicePort.calculateAddonPrice(addonIds);

        BigDecimal subTotal = servicePrice.add(addonPrice);

        BigDecimal discount = BigDecimal.ZERO;

        VoucherContract voucher = voucherPort.getVoucher(voucherCode,subTotal).orElse(null);

        // tính giảm giá nếu voucher hợp lệ
        if (voucher != null && voucher.isValid(subTotal)) {
            discount = subTotal.multiply(BigDecimal.valueOf(voucher.getDiscountPercentage()))
                    .divide(BigDecimal.valueOf(100));
        }

        return new PriceResult(subTotal, discount, subTotal.subtract(discount));
    }

    @Getter
    @AllArgsConstructor
    public static class PriceResult {
        private BigDecimal subTotal;
        private BigDecimal discount;
        private BigDecimal finalTotal;
    }
}
