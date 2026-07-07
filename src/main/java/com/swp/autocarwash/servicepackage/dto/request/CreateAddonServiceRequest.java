package com.swp.autocarwash.servicepackage.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddonServiceRequest {

    private String name;

    private BigDecimal price;

    private Integer durationMinutes;

    private String description;
}