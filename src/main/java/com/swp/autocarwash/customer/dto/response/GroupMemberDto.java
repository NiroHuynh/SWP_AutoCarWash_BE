package com.swp.autocarwash.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberDto {

    private Long customerId;
    private String fullName;
    private String email;
    private String phone;
    private String roleInGroup; // Giá trị trả về: "OWNER" hoặc "MEMBER"
    private LinkedVehicleDto linkedVehicle; // Phương tiện gắn vào gói

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LinkedVehicleDto {
        private Long vehicleId;
        private String licensePlate;
        private String brandName;
        private String color;
    }
}
