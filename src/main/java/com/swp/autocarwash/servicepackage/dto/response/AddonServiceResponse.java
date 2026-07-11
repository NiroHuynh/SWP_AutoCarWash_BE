package com.swp.autocarwash.servicepackage.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddonServiceResponse {

    private Integer id;

    private String name;

    private BigDecimal price;

    private Integer durationMinutes;

    private String description;
}