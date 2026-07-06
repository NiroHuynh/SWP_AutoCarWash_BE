package com.swp.autocarwash.wash.service;

import com.swp.autocarwash.wash.dto.request.CreateWashLaneRequest;
import com.swp.autocarwash.wash.dto.response.CreateWashLaneResponse;
import com.swp.autocarwash.wash.dto.response.WashLaneResponse;

import java.util.List;

public interface WashLaneService {
    CreateWashLaneResponse createWashLane(CreateWashLaneRequest request);
    List<WashLaneResponse> getLanesByStation(Integer stationId);
    void deleteWashLane(Integer laneId);
}
