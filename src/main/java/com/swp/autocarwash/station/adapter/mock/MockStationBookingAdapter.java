package com.swp.autocarwash.station.adapter.mock;

import com.swp.autocarwash.booking.port.StationPort;
import com.swp.autocarwash.common.contract.station.StationContract;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MockStationBookingAdapter implements StationPort {
    @Override
    public StationContract getStationById(Integer stationId) {
        return null;
    }
}
