package com.swp.autocarwash.servicepackage.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddonServiceRequest {

    @NotBlank(message = "Addon service name is required")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Addon service price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Duration is required")
    @Min(value = 0, message = "Duration must be 0 or positive")
    private Integer durationMinutes;

    private String description;
}