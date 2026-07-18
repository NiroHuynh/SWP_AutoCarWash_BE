package com.swp.autocarwash.wash.service;

import com.swp.autocarwash.wash.dto.request.CreateWashLaneRequest;
import com.swp.autocarwash.wash.dto.response.CreateWashLaneResponse;
import com.swp.autocarwash.wash.dto.response.WashLaneResponse;

import java.util.List;

public interface WashLaneService {
    CreateWashLaneResponse createWashLane(CreateWashLaneRequest request);
    List<WashLaneResponse> getLanesByStation(Integer stationId);
    void deleteWashLane(Integer laneId);

    /**
     * Chuyển 1 làn sang MAINTENANCE (bảo trì) hoặc gỡ về AVAILABLE.
     *
     * @param laneId      id làn cần đổi trạng thái
     * @param stationId   id station của staff đang thao tác (kiểm tra làn thuộc đúng station)
     * @param maintenance true = bật bảo trì, false = gỡ bảo trì
     */
    void setMaintenance(Integer laneId, Integer stationId, boolean maintenance);
}
