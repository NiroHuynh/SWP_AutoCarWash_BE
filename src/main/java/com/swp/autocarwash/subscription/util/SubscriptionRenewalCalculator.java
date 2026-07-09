package com.swp.autocarwash.subscription.util;

import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;

import java.time.LocalDate;

/**
 * Chức năng: Tính endDate mới + kỳ hạn hiển thị của gói Unlimited sau khi
 * kích hoạt (đăng ký mới) hoặc gia hạn — dùng chung bởi
 * {@code SubscriptionRenewalListener} (áp dụng thật khi thanh toán xong) và
 * {@code SubscriptionPaymentServiceImpl} (hiển thị preview cho FE trước khi
 * thanh toán), để 2 nơi luôn tính ra cùng một kết quả.
 *
 * <p>Quy tắc: gói đang ACTIVE và chưa hết hạn (tính tới "today") thì cộng dồn
 * nối tiếp endDate hiện có; ngược lại (PENDING lần đầu hoặc EXPIRED) thì tính
 * lại từ "today".</p>
 *
 * <p><b>Lưu ý phân biệt 2 khái niệm ngày bắt đầu:</b>
 * <ul>
 *   <li>{@link #calculateEntityStartDate} — startDate lưu trong DB của
 *       {@code UnlimitSubscription}, chỉ là ngày kích hoạt LẦN ĐẦU của cả
 *       vòng đời gói; khi gia hạn cộng dồn, field này KHÔNG đổi.</li>
 *   <li>{@link #calculateDisplayPeriodStart} — ngày bắt đầu của KỲ HẠN MỚI
 *       vừa gia hạn, dùng để hiển thị trên hóa đơn (vd: gói hết hạn 10/7,
 *       gia hạn hôm 9/7 → hóa đơn phải hiện kỳ hạn mới "10/7 → 10/8", không
 *       phải ngày kích hoạt gốc của gói).</li>
 * </ul>
 *
 * @author Ngân
 * @version 1.0
 */
public final class SubscriptionRenewalCalculator {

    private SubscriptionRenewalCalculator() {
    }

    public static boolean isCumulative(UnlimitSubscription subscription, LocalDate today) {
        return subscription.getStatus() == SubscriptionStatus.ACTIVE
                && !subscription.getEndDate().isBefore(today);
    }

    /** startDate thực lưu DB: giữ nguyên khi cộng dồn, chỉ đổi thành "today" khi kích hoạt mới/hết hạn. */
    public static LocalDate calculateEntityStartDate(UnlimitSubscription subscription, LocalDate today) {
        return isCumulative(subscription, today) ? subscription.getStartDate() : today;
    }

    /** Ngày bắt đầu của kỳ hạn mới vừa gia hạn — dùng để hiển thị hóa đơn (không phải startDate gốc). */
    public static LocalDate calculateDisplayPeriodStart(UnlimitSubscription subscription, LocalDate today) {
        return isCumulative(subscription, today) ? subscription.getEndDate() : today;
    }

    public static LocalDate calculateEndDate(UnlimitSubscription subscription, LocalDate today) {
        int durationDays = subscription.getSubscriptionPlan().getDurationDays();
        return isCumulative(subscription, today)
                ? subscription.getEndDate().plusDays(durationDays)
                : today.plusDays(durationDays);
    }
}
