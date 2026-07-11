package com.swp.autocarwash.customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchInvitedCustomerResponse {

    private Long customerId;
    private String fullName;
    private String phone;
    private String email;
    private List<VehicleDto> vehicles; // Danh sách xe để FE bỏ vào Dropdown

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleDto {
        private Long id;
        private String licensePlate;
        private String brandName;
        private String color;
    }
}
