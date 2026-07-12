package com.swp.autocarwash.station.service;



import com.swp.autocarwash.station.dto.response.StationResponse;
import com.swp.autocarwash.station.entity.Station;

import java.util.List;

/**
 *
 * Chức năng: StationService định nghĩa các nghiệp vụ liên quan đến station trong hệ thống.
 *
 * Interface này đóng vai trò business layer, xử lý logic trung gian giữa controller
 * và repository, đảm bảo dữ liệu station được truy xuất đúng theo commune và format response.
 *
 * @author Phong
 * @version 1.0
 */
public interface StationService {

    /**
     *
     * Chức năng: Lấy danh sách station theo communeId.
     *
     * Quy trình:
     * - Validate communeId (kiểm tra null, hợp lệ, tồn tại nếu có rule).
     * - Truy vấn danh sách station theo communeId từ repository.
     * - Áp dụng business rule nếu cần (filter station active, available,...).
     * - Map entity sang StationResponse DTO.
     * - Trả về danh sách station cho client.
     *
     * @param communeId id của commune cần lấy danh sách station
     *
     * @return danh sách StationResponse thuộc commune
     *
     * @author Phong
     * @version 1.0
     */
    List<StationResponse> getStationsByCommune(Integer communeId);

    Station getStationById(Integer stationId);

    /**
     * Chức năng: Danh sách phẳng toàn bộ station chưa xóa, dùng cho dropdown
     * "Chi nhánh" trên FE (không phụ thuộc communeId).
     */
    List<StationResponse> getAllStations();
}
