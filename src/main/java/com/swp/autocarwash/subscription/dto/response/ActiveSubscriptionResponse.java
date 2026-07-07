package com.swp.autocarwash.subscription.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Chức năng: Thông tin gói subscription đang hoạt động của khách hàng, hiển
 * thị trên FE (tên gói, ngày hết hạn, số ngày còn lại).
 *
 * @author Ngân
 * @version 1.0
 */
@Data
@Builder
public class ActiveSubscriptionResponse {

    private String planName;

    /** UNLIMITED / FAMILY. */
    private String planType;

    private LocalDate startDate;

    /** = endDate của gói. */
    private LocalDate expiryDate;

    private Long daysRemaining;
}
