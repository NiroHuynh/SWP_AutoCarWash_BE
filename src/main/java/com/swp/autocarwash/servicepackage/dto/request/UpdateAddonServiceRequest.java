package com.swp.autocarwash.servicepackage.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddonServiceRequest {

    private String name;

    private BigDecimal price;

    private Integer durationMinutes;

    private String description;
}