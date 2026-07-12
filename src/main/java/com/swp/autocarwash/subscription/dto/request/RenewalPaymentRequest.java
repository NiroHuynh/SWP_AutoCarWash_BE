package com.swp.autocarwash.subscription.dto.request;


import com.swp.autocarwash.payment.entity.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RenewalPaymentRequest {

    @NotNull(message = "PAYMENT_STATUS_REQUIRED")
    private String paymentStatus;

}