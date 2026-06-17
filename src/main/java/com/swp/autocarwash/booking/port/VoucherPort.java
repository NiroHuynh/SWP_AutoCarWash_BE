package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.common.contract.promotion.VoucherContract;

import java.util.List;
import java.util.Optional;

public interface VoucherPort {
    List<VoucherContract> getValidVouchers(Integer customerId);

    boolean validate(Integer voucherId, Integer customerId);

    /**
     * Get voucher by code
     */
    Optional<VoucherContract> getVoucher(String code);
}