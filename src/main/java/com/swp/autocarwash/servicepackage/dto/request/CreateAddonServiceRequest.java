package com.swp.autocarwash.servicepackage.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddonServiceRequest {

    @NotBlank(message = "ADDON_SERVICE_NAME_REQUIRED")
    private String name;

    @NotNull(message = "INVALID_ADDON_PRICE")
    @DecimalMin(value = "0.01", message = "INVALID_ADDON_PRICE")
    private BigDecimal price;

    @NotNull(message = "INVALID_ADDON_DURATION")
    @Positive(message = "INVALID_ADDON_DURATION")
    private Integer durationMinutes;

    @NotNull(message = "INVALID_SERVICE_CATEGORY")
    private Integer serviceCategoryId;
}
