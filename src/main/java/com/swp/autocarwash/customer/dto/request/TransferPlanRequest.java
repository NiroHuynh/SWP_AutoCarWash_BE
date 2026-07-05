package com.swp.autocarwash.customer.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferPlanRequest {
    private Long sourceVehicleId;
    private Long targetVehicleId;
}
