package com.swp.autocarwash.staff.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class WalkInFormDataResponse {
    private List<ServicePackageDTO> servicePackages;
    private List<AddonServiceDTO> addonServices;

    @Data
    @Builder
    public static class ServicePackageDTO {
        private Integer id;
        private String name;
        private BigDecimal basePrice;
        private Integer requiredSlot; // Để FE biết gói này chiếm bao nhiêu slot (ví dụ: 2 slot = 30 phút)
        private String description;
    }

    @Data
    @Builder
    public static class AddonServiceDTO {
        private Integer id;
        private String name;
        private BigDecimal price;
        private String description;
        private Integer durationMinutes;
        private List<Integer> includedInPackageIds;
    }
}
