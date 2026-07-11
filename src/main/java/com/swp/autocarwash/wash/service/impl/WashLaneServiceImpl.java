package com.swp.autocarwash.wash.service.impl;

import com.swp.autocarwash.booking.repository.BookingSlotRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.station.entity.Station;
import com.swp.autocarwash.station.repository.StationRepository;
import com.swp.autocarwash.wash.dto.request.CreateWashLaneRequest;
import com.swp.autocarwash.wash.dto.response.CreateWashLaneResponse;
import com.swp.autocarwash.wash.dto.response.WashLaneResponse;
import com.swp.autocarwash.wash.entity.WashLane;
import com.swp.autocarwash.wash.entity.enums.WashLaneStatus;
import com.swp.autocarwash.wash.mapper.WashLaneMapper;
import com.swp.autocarwash.wash.repository.custom.WashLaneRepository;
import com.swp.autocarwash.wash.service.WashLaneService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WashLaneServiceImpl implements WashLaneService {

    private final StationRepository stationRepository;
    private final WashLaneRepository washLaneRepository;
    private final WashLaneMapper washLaneMapper;
    private final BookingSlotRepository bookingSlotRepository;

    /**
     * Chức năng: Đồng bộ capacity theo số làn active hiện tại của station —
     * gọi mỗi khi thêm/xóa làn để station.max_wash_capacity và
     * booking_slot.max_capacity (slot tương lai) luôn khớp số làn thật.
     *
     * @author Ngân
     * @version 1.0
     */
    private void syncCapacity(Station station) {
        long activeLaneCount = washLaneRepository.countByStationIdAndIsDeletedFalse(station.getId());

        station.setMaxWashCapacity((int) activeLaneCount);
        stationRepository.save(station);

        bookingSlotRepository.updateFutureSlotsCapacity(
                station.getId(), (int) activeLaneCount, LocalDate.now(), LocalTime.now());
    }

    @Override
    @Transactional
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
        syncCapacity(station);
        return CreateWashLaneResponse.builder()
                .id(savedLane.getId())
                .stationId(station.getId())
                .laneName(savedLane.getLaneName())
                .status(savedLane.getStatus())
                .bookingWalkinRatio(savedLane.getBookingWalkinRatio())
                .isDeleted(savedLane.getIsDeleted())
                .build();
    }

    @Override
    public List<WashLaneResponse> getLanesByStation(Integer stationId) {
        //Kiểm tra trạm tồn tại
        if (!stationRepository.existsById(stationId)) {
            throw new ResourceNotFoundException(ErrorCode.STATION_NOT_AVAILABLE);
        }

        //Lấy danh sách Entity gốc từ Repo lên như em muốn
        List<WashLane> lanes = washLaneRepository.findByStationIdAndIsDeletedFalse(stationId);

        //chuyển đổi sang List DTO
        return washLaneMapper.toResponseList(lanes);
    }

    @Override
    @Transactional
    public void deleteWashLane(Integer laneId) {
        //Tìm làn xe gốc trong Database theo ID lên trước
        WashLane lane = washLaneRepository.findById(laneId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.LANE_NOT_FOUND));
        // Kiểm tra thủ công: Nếu làn này ĐÃ BỊ XÓA MỀM rồi thì cũng coi như không tồn tại (Báo lỗi 404)
        if (Boolean.TRUE.equals(lane.getIsDeleted())) {
            throw new ResourceNotFoundException(ErrorCode.LANE_NOT_FOUND);
        }
        if (WashLaneStatus.WASHING.name().equalsIgnoreCase(lane.getStatus())) {
            throw new BusinessException(ErrorCode.CANNOT_DELETE_WASHING_LANE);
        }
        lane.setIsDeleted(true);
        washLaneRepository.save(lane);
        syncCapacity(lane.getStation());
    }
}
