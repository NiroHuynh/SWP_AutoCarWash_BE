package com.swp.autocarwash.wash.service.impl;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.station.entity.Station;
import com.swp.autocarwash.station.repository.StationRepository;
import com.swp.autocarwash.wash.dto.request.CreateWashLaneRequest;
import com.swp.autocarwash.wash.dto.response.CreateWashLaneResponse;
import com.swp.autocarwash.wash.entity.WashLane;
import com.swp.autocarwash.wash.repository.custom.WashLaneRepository;
import com.swp.autocarwash.wash.service.WashLaneService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WashLaneServiceImpl implements WashLaneService {

    private final StationRepository stationRepository;
    private final WashLaneRepository washLaneRepository;


    @Override
    public CreateWashLaneResponse createWashLane(CreateWashLaneRequest request) {
        // 1. [AC01]: Kiểm tra trạm tồn tại và hoạt động
        Station station = stationRepository.findByIdAndIsDeletedFalse(request.getStationId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.STATION_NOT_AVAILABLE));
        if (Boolean.FALSE.equals(station.getIsOperating())) {
            throw new BusinessException(ErrorCode.STATION_NOT_AVAILABLE);
        }
        boolean isDuplicateName = washLaneRepository.existsByStationIdAndLaneName(request.getStationId(), request.getLaneName());
        if(isDuplicateName) {
            throw new BusinessException(ErrorCode.LANE_NAME_ALREADY_EXISTS);
        }

        WashLane newLane = WashLane.builder()
                .station(station)
                .laneName(request.getLaneName())
                .status(request.getStatus().toUpperCase())
                .bookingWalkinRatio(request.getBookingWalkinRatio())
                .isDeleted(false)
                .build();
        WashLane savedLane = washLaneRepository.save(newLane);
        return CreateWashLaneResponse.builder()
                .id(savedLane.getId())
                .stationId(station.getId())
                .laneName(savedLane.getLaneName())
                .status(savedLane.getStatus())
                .bookingWalkinRatio(savedLane.getBookingWalkinRatio())
                .isDeleted(savedLane.getIsDeleted())
                .build();
    }
}
