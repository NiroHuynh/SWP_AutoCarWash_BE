package com.swp.autocarwash.customer.dto.response;

import lombok.*;


/**
 *
 * Response DTO trả về thông tin xe
 *
 * @author Phong
 * @version 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleResponse {
    
    /**
     * Customer id
     */
    private Integer customerId;



    /**
     * License plate
     */
    private String licensePlate;



    /**
     * Brand name
     */
    private String brandName;



    /**
     * Vehicle color
     */
    private String color;

}