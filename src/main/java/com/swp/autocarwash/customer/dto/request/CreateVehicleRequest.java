package com.swp.autocarwash.customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


/**
 *
 * Request DTO dùng để tạo phương tiện mới
 *
 * @author Phong
 * @version 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleRequest {

    /**
     * Biển số xe
     */
    @NotBlank(
            message = "License plate is required"
    )
    @Size(
            max = 20,
            message = "License plate max length is 20"
    )
    private String licensePlate;



    /**
     * Tên hãng xe
     */
    @NotBlank(
            message = "Brand name is required"
    )
    @Size(
            max = 50
    )
    private String brandName;



    /**
     * Màu xe
     */
    @Size(
            max = 30
    )
    private String color;

}
