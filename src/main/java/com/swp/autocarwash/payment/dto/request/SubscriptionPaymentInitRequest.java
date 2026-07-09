package com.swp.autocarwash.payment.dto.request;

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
}
