package com.swp.autocarwash.station.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommuneResponse {

    private Integer id;
    private String communeName;
    private Integer provinceId;
}
