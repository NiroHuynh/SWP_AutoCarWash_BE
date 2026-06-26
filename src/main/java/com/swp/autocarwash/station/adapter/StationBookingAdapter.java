package com.swp.autocarwash.station.adapter;

import com.swp.autocarwash.booking.port.StationPort;
import com.swp.autocarwash.common.contract.station.StationContract;
import com.swp.autocarwash.station.entity.Station;
import com.swp.autocarwash.station.service.StationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("pro")
public class StationBookingAdapter implements StationPort {

    private final StationService stationService;

    private final ModelMapper modelMapper;

    @Override
    public StationContract getStationById(Integer stationId) {
        Station station = stationService.getStationById(stationId);
        return modelMapper.map(station,StationContract.class);
    }
}
