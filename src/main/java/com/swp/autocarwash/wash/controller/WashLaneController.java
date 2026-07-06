package com.swp.autocarwash.wash.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.wash.dto.request.CreateWashLaneRequest;
import com.swp.autocarwash.wash.dto.response.CreateWashLaneResponse;
import com.swp.autocarwash.wash.entity.WashLane;
import com.swp.autocarwash.wash.service.WashLaneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
