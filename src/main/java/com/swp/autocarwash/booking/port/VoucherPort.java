package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.common.contract.promotion.VoucherContract;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VoucherPort {
    List<VoucherContract> getValidVouchers(Integer customerId);

    boolean validate(Integer voucherId, Integer customerId);

    /**
     * Validate voucher + trả discount %
     */
    VoucherContract getDiscountPercent(String code, Integer amount);


    /**
     * Get voucher by code
     */
    Optional<VoucherContract> getVoucher(String code, BigDecimal orderValue);
}