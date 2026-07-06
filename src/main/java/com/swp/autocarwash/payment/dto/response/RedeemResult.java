package com.swp.autocarwash.payment.dto.response;

import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedeemResult {

    private BigDecimal redeemAmount;

    private BigDecimal usedPoints;

}
