package com.swp.autocarwash.loyalty.port;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Port cho phep module loyalty doc tong chi tieu cua khach hang tu module payment,
 * ma khong phu thuoc truc tiep vao entity/repository cua payment (kien truc hexagonal).
 *
 * @author KimNgan
 * @version 1.0
 */
public interface SpendingPort {

    /**
     * Tong chi tieu da thanh toan cua khach (Booking + Subscription) trong khoang [from, to).
     *
     * @param customerId id khach hang
     * @param from       moc bat dau (inclusive)
     * @param to         moc ket thuc (exclusive)
     * @return tong tien (>= 0), khong bao gio null
     */
    BigDecimal getTotalSpending(Long customerId, Instant from, Instant to);
}
