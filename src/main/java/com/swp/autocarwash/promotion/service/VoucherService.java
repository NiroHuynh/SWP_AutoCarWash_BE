package com.swp.autocarwash.promotion.service;

import com.swp.autocarwash.common.contract.promotion.VoucherContract;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Voucher business service
 *
 * @author Phong
 * @version 1.0
 */
public interface VoucherService {

    List<VoucherContract> getValidVouchers(Integer customerId);

    VoucherContract getVoucher(String code, BigDecimal orderValue);

    boolean validate(Integer voucherId, Integer customerId);

    VoucherContract calculateDiscount(String code, BigDecimal amount);
}
