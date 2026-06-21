package com.swp.autocarwash.station.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.station.dto.response.CommuneResponse;
import com.swp.autocarwash.station.service.CommuneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 *
 * Chức năng: CommuneController cung cấp các API liên quan đến commune
 * theo province trong hệ thống.
 *
 * Controller này tiếp nhận HTTP request từ client, gọi CommuneService xử lý
 * nghiệp vụ và trả về dữ liệu commune thông qua ApiResponse.
 *
 * Base URL: /api/provinces
 *
 * @author Phong
 * @version 1.0
 */
@RestController
@RequestMapping("/api/provinces")
@RequiredArgsConstructor
public class CommuneController {

    private final CommuneService communeService;

    /**
     *
     * Chức năng: Lấy danh sách commune thuộc một province.
     *
     * Quy trình:
     * - Nhận provinceId từ path variable.
     * - Gọi CommuneService để truy vấn danh sách commune.
     * - Nhận kết quả xử lý từ service layer.
     * - Trả về danh sách commune thông qua ApiResponse.
     *
     * @param provinceId id của province cần lấy danh sách commune
     *
     * @return danh sách CommuneResponse thuộc province
     *
     * @author Phong
     * @version 1.0
     */
    @GetMapping("/{provinceId}/communes")
    public ApiResponse<List<CommuneResponse>> getCommunes(@PathVariable Integer provinceId) {

        List<CommuneResponse> data = communeService.getCommunesByProvinceId(provinceId);

        return ApiResponse.success(
                "Get communes successfully",
                data
        );
    }
}
