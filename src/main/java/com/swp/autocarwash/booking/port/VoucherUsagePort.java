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

    /**
     * Trả lại voucher đã consume cho một booking (khi booking bị hủy vì quá hạn
     * thanh toán cọc): xóa voucher_usage và giảm usedCount của voucher.
     * Không làm gì nếu booking không dùng voucher.
     */
    void releaseVoucher(Long bookingId);
}
