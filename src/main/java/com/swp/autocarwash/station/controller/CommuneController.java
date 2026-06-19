package com.swp.autocarwash.station.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.station.dto.response.CommuneResponse;
import com.swp.autocarwash.station.service.CommuneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/provinces")
@RequiredArgsConstructor
public class CommuneController {

    private final CommuneService communeService;

    /**
     * API: GET /api/provinces/{provinceId}/communes
     * Purpose: lấy danh sách commune theo province
     */
    @GetMapping("/{provinceId}/communes")
    public ApiResponse<List<CommuneResponse>> getCommunes(@PathVariable Integer provinceId) {

        List<CommuneResponse> data = communeService.getCommunesByProvinceId(provinceId);

        return ApiResponse.success(
                "Get communes successfully",
                data
        );
    }
}
