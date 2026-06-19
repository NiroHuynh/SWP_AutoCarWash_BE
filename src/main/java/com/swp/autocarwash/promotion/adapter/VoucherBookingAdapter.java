package com.swp.autocarwash.promotion.adapter;

import com.swp.autocarwash.booking.port.VoucherPort;
import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import com.swp.autocarwash.promotion.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 *
 * Production voucher adapter
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("pro")
@RequiredArgsConstructor
public class VoucherBookingAdapter implements VoucherPort {

    private final VoucherService voucherService;

    @Override
    public List<VoucherContract> getValidVouchers(Integer customerId) {
        return voucherService.getValidVouchers(customerId);
    }

    @Override
    public boolean validate(Integer voucherId, Integer customerId) {
        return voucherService.validate(voucherId, customerId);
    }

    @Override
    public Optional<VoucherContract> getVoucher(String code,BigDecimal orderValue) {
        return Optional.of(
                voucherService.getVoucher(code, orderValue)
        );
    }

    @Override
    public VoucherContract getDiscountPercent(String code, Integer amount) {
        return voucherService.calculateDiscount(code, BigDecimal.valueOf(amount));
    }
}
