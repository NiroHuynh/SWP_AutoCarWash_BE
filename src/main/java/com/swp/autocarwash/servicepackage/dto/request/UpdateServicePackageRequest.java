package com.swp.autocarwash.servicepackage.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateServicePackageRequest {

    @NotBlank(message = "Package name is required")
    private String name;

    @NotNull(message = "Package price is required")
    @DecimalMin(value = "0.01", message = "Package price must be greater than 0")
    private BigDecimal basePrice;

    private String description;

    @NotNull(message = "Duration is required")
    @Min(value = 0, message = "Duration must be 0 or positive")
    private Integer durationMinutes;

    @NotEmpty(message = "Please select at least one included service")
    private List<Integer> addonIds;
}