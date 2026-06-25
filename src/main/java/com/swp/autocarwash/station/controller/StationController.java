package com.swp.autocarwash.station.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.station.dto.response.StationResponse;
import com.swp.autocarwash.station.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 *
 * Chức năng: StationController cung cấp API liên quan đến station trong hệ thống.
 *
 * Controller này chịu trách nhiệm nhận request từ client, gọi StationService xử lý
 * nghiệp vụ và trả về danh sách station theo commune thông qua ApiResponse.
 *
 * Base URL: /api/communes
 *
 * @author Phong
 * @version 1.0
 */
@RestController
@RequestMapping("/api/communes")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    /**
     *
     * Chức năng: Lấy danh sách station theo communeId.
     *
     * Quy trình:
     * - Nhận communeId từ path variable.
     * - Gọi StationService để truy vấn danh sách station thuộc commune.
     * - Nhận dữ liệu từ service layer đã được xử lý nghiệp vụ.
     * - Wrap dữ liệu vào ApiResponse để đảm bảo format response thống nhất.
     * - Trả về danh sách station cho client.
     *
     * @param communeId id của commune cần lấy danh sách station
     *
     * @return danh sách StationResponse thuộc commune
     *
     * @author Phong
     * @version 1.0
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