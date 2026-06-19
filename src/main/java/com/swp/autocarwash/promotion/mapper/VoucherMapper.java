package com.swp.autocarwash.promotion.mapper;

import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import com.swp.autocarwash.promotion.entity.Voucher;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 *
 * Mapper Voucher entity → VoucherContract
 *
 * @author Phong
 * @version 1.0
 */
@Component
public class VoucherMapper {

    private final ModelMapper modelMapper;

    public VoucherMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Convert entity to contract
     */
    public VoucherContract toContract(Voucher voucher) {
        VoucherContract contract = modelMapper.map(voucher, VoucherContract.class);

        contract.setValid(isValid(voucher));

        return contract;
    }

    /**
     * Validate voucher business rules
     */
    private boolean isValid(Voucher voucher) {

        Instant now = Instant.now();

        return voucher.getStatus().equals("ACTIVE")
                && voucher.getStartDate().isBefore(now)
                && voucher.getExpiryDate().isAfter(now)
                && voucher.getUsedCount() < voucher.getUsageLimit();
    }
}