package com.swp.autocarwash.common.contract.servicepackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicePackageContract {
    private Integer id;
    private String name;
    private BigDecimal basePrice;
    private Integer durationMinutes;
}
