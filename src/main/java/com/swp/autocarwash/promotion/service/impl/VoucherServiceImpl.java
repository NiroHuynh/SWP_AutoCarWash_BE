package com.swp.autocarwash.promotion.service.impl;

import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.promotion.entity.Voucher;
import com.swp.autocarwash.promotion.mapper.VoucherMapper;
import com.swp.autocarwash.promotion.repository.VoucherRepository;
import com.swp.autocarwash.promotion.repository.VoucherUsageRepository;
import com.swp.autocarwash.promotion.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 *
 * Voucher service implementation
 *
 * @author Phong
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherUsageRepository usageRepository;
    private final VoucherMapper voucherMapper;

    /**
     * Get all valid vouchers for customer
     */
    @Override
    @Transactional(readOnly = true)
    public List<VoucherContract> getValidVouchers(Integer customerId) {

        List<Voucher> vouchers = voucherRepository.findAll();

        return vouchers.stream()
                .map(voucherMapper::toContract)
                .toList();
    }

    /**
     * Get voucher by code with validation
     */
    @Override
    public VoucherContract getVoucher(String code, BigDecimal orderValue) {

        Voucher voucher = voucherRepository.findByVoucherCode(code)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.VOUCHER_NOT_FOUND)
                );

        if (voucher.getExpiryDate().isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.VOUCHER_EXPIRED);
        }

        if (orderValue.compareTo(voucher.getMinOrderValue()) < 0) {
            throw new BusinessException(ErrorCode.VOUCHER_NOT_APPLICABLE);
        }

        return voucherMapper.toContract(voucher);
    }

    /**
     * Validate voucher usage by customer
     */
    @Override
    public boolean validate(Integer voucherId, Integer customerId) {

        long usage = usageRepository
                .countByVoucherIdAndCustomerId(voucherId, customerId);

        if (usage > 0) {
            throw new BusinessException(ErrorCode.VOUCHER_USAGE_LIMIT_REACHED);
        }

        return true;
    }

    /**
     * Calculate discount result
     */
    @Override
    public VoucherContract calculateDiscount(String code, BigDecimal amount) {

        Voucher voucher = voucherRepository.findByVoucherCode(code)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.VOUCHER_NOT_FOUND)
                );

        VoucherContract contract = voucherMapper.toContract(voucher);

        if (!contract.isValid(amount)) {
            throw new BusinessException(ErrorCode.VOUCHER_NOT_APPLICABLE);
        }

        return contract;
    }
}