package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.common.contract.promotion.VoucherContract;

import java.util.List;

public interface VoucherPort {
    List<VoucherContract> getValidVouchers(Integer customerId);
}