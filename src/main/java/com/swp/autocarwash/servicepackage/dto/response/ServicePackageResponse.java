package com.swp.autocarwash.servicepackage.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicePackageResponse {

    private Integer id;

    private String name;

    private BigDecimal basePrice;

    private String description;
}