package com.swp.autocarwash.promotion.adapter.mock;

import com.swp.autocarwash.booking.port.VoucherPort;
import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

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
}
