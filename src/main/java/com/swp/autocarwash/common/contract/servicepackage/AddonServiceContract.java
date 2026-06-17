package com.swp.autocarwash.common.contract.servicepackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AddonServiceContract {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer durationMinutes;
}
