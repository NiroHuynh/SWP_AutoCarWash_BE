package com.swp.autocarwash.promotion.adapter.mock;

import com.swp.autocarwash.booking.port.VoucherPort;
import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class MockVoucherBookingAdapter implements VoucherPort {

    /**
     * Mock voucher theo customer
     * (giả lập tất cả voucher hợp lệ)
     */
    @Override
    public List<VoucherContract> getValidVouchers(Integer customerId) {
        return List.of(
                new VoucherContract(1, "NEW10", 10, new BigDecimal("50000")),
                new VoucherContract(2, "GOLD20", 20, new BigDecimal("150000")),
                new VoucherContract(3, "WELCOME5", 5, new BigDecimal("30000"))
        );
    }

    @Override
    public boolean validate(Integer voucherId, Integer customerId) {
        return false;
    }

    @Override
    public Optional<VoucherContract> getVoucher(String code) {

        if (code == null) return Optional.empty();

        VoucherContract v = new VoucherContract();
        v.setVoucherCode(code);
        v.setDiscountPercentage(15);

        return Optional.of(v);
    }
}
