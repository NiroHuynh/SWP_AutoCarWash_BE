package com.swp.autocarwash.booking.port;


import com.swp.autocarwash.common.contract.station.StationContract;

public interface StationPort {
    StationContract getStationById(Integer stationId);
}
