package com.swp.autocarwash.staff.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CalculateInvoiceRequest {
    private Long customerId;
    private String licensePlate;
    private Integer servicePackageId;
    private List<Integer> addonIds;
}
