package com.swp.autocarwash.station.service;



import com.swp.autocarwash.station.dto.response.ProvinceResponse;

import java.util.List;

/**
 *
 * Chức năng: ProvinceService định nghĩa các nghiệp vụ liên quan đến province (tỉnh/thành phố).
 *
 * Interface này đóng vai trò business layer, xử lý logic trung gian giữa controller
 * và repository trước khi trả dữ liệu về client.
 *
 * @author Phong
 * @version 1.0
 */
public interface ProvinceService {
    /**
     *
     * Chức năng: Lấy toàn bộ danh sách province trong hệ thống.
     *
     * Quy trình:
     * - Thực hiện truy vấn dữ liệu province từ repository layer.
     * - Áp dụng business logic nếu cần (filter, sort, active status,...).
     * - Map entity sang ProvinceResponse DTO.
     * - Trả về danh sách province cho client.
     *
     * @return danh sách ProvinceResponse
     *
     * @author Phong
     * @version 1.0
     */
    List<ProvinceResponse> getAllProvinces();
}
