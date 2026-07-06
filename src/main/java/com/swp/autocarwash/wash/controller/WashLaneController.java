package com.swp.autocarwash.wash.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.wash.dto.request.CreateWashLaneRequest;
import com.swp.autocarwash.wash.dto.response.CreateWashLaneResponse;
import com.swp.autocarwash.wash.dto.response.WashLaneResponse;
import com.swp.autocarwash.wash.entity.WashLane;
import com.swp.autocarwash.wash.service.WashLaneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/stations")
public class WashLaneController {

    private final WashLaneService washLaneService;

    @PostMapping("/lanes")
    public ResponseEntity<ApiResponse<CreateWashLaneResponse>> createWashLane(@Valid @RequestBody CreateWashLaneRequest request){
        CreateWashLaneResponse response = washLaneService.createWashLane(request);
        return ResponseEntity.ok(ApiResponse.success("Add wash lane successfully", response));
    }

    @GetMapping("/{stationId}/lanes")
    public ResponseEntity<ApiResponse<List<WashLaneResponse>>> getLanesByStation(@PathVariable Integer stationId) {

        // Gọi xuống tầng Service để lấy danh sách thực thể đã được MapStruct chuyển đổi thành DTO
        List<WashLaneResponse> response = washLaneService.getLanesByStation(stationId);
        return ResponseEntity.ok(ApiResponse.success("List of wash lane in this station",response));
    }

    @DeleteMapping("/lanes/{laneId}")
    public ResponseEntity<?> deleteWashLane(@PathVariable Integer laneId) {

        // Gọi xuống Service xử lý chốt chặn logic (Check tồn tại -> Check trạng thái WASHING -> Đổi cờ is_deleted)
        washLaneService.deleteWashLane(laneId);

        // Trả về thông báo thành công rực rỡ cho Frontend bật Toast Message xanh
        return ResponseEntity.ok(ApiResponse.success("Deleted washlane successfully",null));
    }
}
