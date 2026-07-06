package com.swp.autocarwash.wash.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WashLaneResponse {
    private Integer id;
    private String laneName;
    private String status;
    private Integer bookingWalkinRatio;
}
