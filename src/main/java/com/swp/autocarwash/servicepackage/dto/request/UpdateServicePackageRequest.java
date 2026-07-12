package com.swp.autocarwash.servicepackage.dto.request;

import jakarta.validation.constraints.DecimalMin;
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

    @NotEmpty(message = "Please select at least one included service")
    private List<Integer> addonIds;
}