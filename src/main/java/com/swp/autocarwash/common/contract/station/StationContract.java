package com.swp.autocarwash.common.contract.station;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationContract {
    private Integer id;
    private String stationName;
    private String address;
}
