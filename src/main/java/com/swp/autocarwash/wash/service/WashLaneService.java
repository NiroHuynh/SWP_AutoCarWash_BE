package com.swp.autocarwash.wash.service;

import com.swp.autocarwash.wash.dto.request.CreateWashLaneRequest;
import com.swp.autocarwash.wash.dto.response.CreateWashLaneResponse;

public interface WashLaneService {
    CreateWashLaneResponse createWashLane(CreateWashLaneRequest request);
}
