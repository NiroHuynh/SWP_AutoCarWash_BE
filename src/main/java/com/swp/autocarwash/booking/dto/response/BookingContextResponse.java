package com.swp.autocarwash.booking.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class BookingContextResponse {

    private Integer stationId;
    private BookingWindowDTO bookingWindow;

    private List<VehicleDTO> vehicles;
    private List<ServicePackageDTO> servicePackages;
    private List<AddonServiceDTO> addonServices;
    private List<VoucherDTO> vouchers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingWindowDTO {
        private LocalDate minDate;
        private LocalDate maxDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleDTO {
        private Integer id;
        private String licensePlate;
        private String brandName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServicePackageDTO {
        private Integer id;
        private String name;
        private BigDecimal basePrice;
        private Integer durationMinutes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddonServiceDTO {
        private Integer id;
        private String name;
        private BigDecimal price;
        private Integer durationMinutes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoucherDTO {
        private Integer id;
        private String voucherCode;
        private Integer discountPercentage;
        private BigDecimal minOrderValue;
    }
}
