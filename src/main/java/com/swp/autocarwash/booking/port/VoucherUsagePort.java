package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.customer.entity.Customer;

public interface VoucherUsagePort {
    /**
     * Ghi nhận voucher được sử dụng.
     *
     * Flow:
     * - Validate voucher.
     * - Kiểm tra customer đã dùng chưa.
     * - Tạo voucher_usage.
     *
     */
    void consumeVoucher(
            Long voucherId,
            Booking booking
    );
}
