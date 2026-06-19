package com.swp.autocarwash.station.service;



import com.swp.autocarwash.station.dto.response.StationResponse;

import java.util.List;

/**
 * Service xử lý business logic liên quan Station
 */
public interface StationService {

    /**
     * Lấy danh sách station theo commune
     *
     * Flow:
     * 1. Validate communeId
     * 2. Query station theo communeId
     * 3. Map entity -> response DTO
     */
    List<StationResponse> getStationsByCommune(Integer communeId);
}
