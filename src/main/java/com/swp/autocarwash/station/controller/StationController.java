package com.swp.autocarwash.station.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.station.dto.response.StationResponse;
import com.swp.autocarwash.station.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Controller xử lý API liên quan Station
 */
@RestController
@RequestMapping("/api/communes")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    /**
     * API:
     * GET /api/communes/{communeId}/stations
     *
     * Mục đích:
     * Trả về danh sách station theo communeId
     */
    @GetMapping("/{communeId}/stations")
    public ApiResponse<List<StationResponse>> getStationsByCommune(
            @PathVariable Integer communeId
    ) {

        List<StationResponse> data = stationService.getStationsByCommune(communeId);

        return ApiResponse.success(
                "Get stations successfully",
                data
        );
    }
}