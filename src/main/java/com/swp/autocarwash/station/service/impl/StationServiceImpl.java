package com.swp.autocarwash.station.service.impl;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.swp.autocarwash.station.dto.response.StationResponse;
import com.swp.autocarwash.station.entity.Station;
import com.swp.autocarwash.station.repository.StationRepository;
import com.swp.autocarwash.station.service.StationService;

import java.util.List;

/**
 * Implement business logic Station
 */
@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final ModelMapper modelMapper;

    /**
     * Lấy station theo communeId
     */
    @Override
    public List<StationResponse> getStationsByCommune(Integer communeId) {

        if (communeId == null || communeId <= 0) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        List<Station> stations = stationRepository.findByCommuneId(communeId);

        if (stations.isEmpty()) {
            throw new BusinessException(ErrorCode.STATION_NOT_FOUND);
        }

        return stations.stream()
                .map(station -> modelMapper.map(station, StationResponse.class))
                .toList();
    }

    @Override
    public Station getStationById(Integer stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(()-> new BusinessException(ErrorCode.STATION_NOT_FOUND));
    }
}
