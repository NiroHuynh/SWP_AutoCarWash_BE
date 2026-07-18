package com.swp.autocarwash.payment.dto.request;

import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Chức năng: Request module subscription gọi sang payment để khởi tạo thanh
 * toán mua gói — tạo SubscriptionInvoice PENDING chờ khách chuyển khoản.
 *
 * @author Ngân
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPaymentInitRequest {

    @NotNull
    private Long customerId;

    /** Giá gói — copy từ SubscriptionPlan.price tại thời điểm mua. */
    @NotNull
    private BigDecimal planPrice;

    /** Tên gói — để FE hiển thị trên màn thanh toán (AC01). */
    private String planName;

    /** Id bản ghi UnlimitSubscription đang chờ thanh toán (nullable nếu gói FAMILY). */
    private Long unlimitSubscriptionId;

    /** Id bản ghi FamilySubscription đang chờ thanh toán (nullable nếu gói UNLIMITED). */
    private Long familySubscriptionId;

    /** Id gói đích của lần thanh toán này — dùng khi renew Family đổi sang gói khác (nullable). */
    private Integer subscriptionPlanId;

    /**
     * REGISTER (đăng ký mới) hoặc RENEW (gia hạn) — set bởi caller
     * (SubscriptionPurchaseServiceImpl) để invoice tạo ra khớp với type check
     * của endpoint xác nhận gia hạn thủ công đã có sẵn trên nhánh này.
     */
    @NotNull
    private SubscriptionInvoiceType type;
}
