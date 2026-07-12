package com.swp.autocarwash.subscription.dto.response;

import com.swp.autocarwash.payment.entity.enums.SubscriptionInvoiceStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewalResponse {

    private Long invoiceId;

    private SubscriptionInvoiceStatus status;

}
