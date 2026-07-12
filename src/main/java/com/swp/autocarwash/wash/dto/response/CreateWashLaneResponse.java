package com.swp.autocarwash.wash.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWashLaneResponse {
    private Integer id;
    private Integer stationId;
    private String laneName;
    private String status;
    private Integer bookingWalkinRatio;
    private Boolean isDeleted;
}
