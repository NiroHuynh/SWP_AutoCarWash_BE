package com.swp.autocarwash.station.service;


import com.swp.autocarwash.station.dto.response.CommuneResponse;

import java.util.List;

/**
 *
 * Chức năng: CommuneService định nghĩa các nghiệp vụ liên quan đến commune trong hệ thống.
 *
 * Interface này đóng vai trò business layer, xử lý logic trung gian giữa controller
 * và repository trước khi trả dữ liệu về client.
 *
 * @author Phong
 * @version 1.0
 */
public interface CommuneService {

    /**
     *
     * Chức năng: Lấy danh sách commune theo provinceId.
     *
     * Quy trình:
     * - Nhận provinceId từ controller.
     * - Thực hiện xử lý business logic (validate province, mapping, filter nếu cần).
     * - Truy vấn dữ liệu commune từ repository layer.
     * - Map entity sang CommuneResponse DTO.
     * - Trả về danh sách commune cho client.
     *
     * @param provinceId id của province cần lấy danh sách commune
     *
     * @return danh sách CommuneResponse thuộc province
     *
     * @author Phong
     * @version 1.0
     */
    List<CommuneResponse> getCommunesByProvinceId(Integer provinceId);
}
