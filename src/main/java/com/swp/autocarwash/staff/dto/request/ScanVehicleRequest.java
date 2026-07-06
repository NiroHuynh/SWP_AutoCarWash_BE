package com.swp.autocarwash.staff.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScanVehicleRequest {

    @NotBlank(message="License plate must not be blank")
    private String licensePlate;

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = (licensePlate != null) ? licensePlate.trim().toUpperCase() : null;
    }

}
