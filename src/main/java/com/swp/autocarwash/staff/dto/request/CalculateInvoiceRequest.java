package com.swp.autocarwash.staff.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CalculateInvoiceRequest {
    private Long customerId;
    @NotBlank(message = "License plate must not be blank")
    private String licensePlate;
    @NotNull(message = "Service package ID must not be null")
    private Integer servicePackageId;
    @NotNull(message = "Station ID must not be null")
    private Integer stationId;
    private List<Integer> addonIds;
}
