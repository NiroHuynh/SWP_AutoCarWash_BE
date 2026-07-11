package com.swp.autocarwash.servicepackage.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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

    private BigDecimal basePrice;

    // SUM(addon.duration_minutes) qua package_addon_mapping
    private Integer durationMinutes;

    // Danh sách addon thuộc package
    private List<Integer> addonIds;
}