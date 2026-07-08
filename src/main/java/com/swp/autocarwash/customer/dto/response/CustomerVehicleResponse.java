package com.swp.autocarwash.customer.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerVehicleResponse {

    private Long id;

    private String licensePlate;

    private String vehicleName;
}
