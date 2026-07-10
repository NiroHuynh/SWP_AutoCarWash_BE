package com.swp.autocarwash.subscription.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferVehicleRequest {

    @NotNull(message = "VEHICLE_REQUIRED")
    private Long vehicleId;

}