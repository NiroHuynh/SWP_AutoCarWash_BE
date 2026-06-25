package com.swp.autocarwash.staff.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Ẩn các trường null khi trả về JSON
public class CheckPhoneResponse {
    private Boolean existed;
    private String customerName;
    private String tierName;
    private Long customerId;
    private List<VehicleDTO> savedVehicles;

    @Getter
    @Setter
    @Builder
    public static class VehicleDTO {
        private Long id;
        private String licensePlate;
        private String brandName;
        private String color;
    }
}
