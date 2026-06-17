package com.swp.autocarwash.station.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO trả về thông tin station cho client
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationResponse {

    private Integer id;

    private String stationName;

    private String address;

    private boolean isOperating;
}
