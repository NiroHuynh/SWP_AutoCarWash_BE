package com.swp.autocarwash.station.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.station.dto.response.ProvinceResponse;
import com.swp.autocarwash.station.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * Chức năng: ProvinceController cung cấp API liên quan đến province (tỉnh/thành phố)
 * trong hệ thống.
 *
 * Controller này chịu trách nhiệm nhận request từ client, gọi ProvinceService xử lý
 * nghiệp vụ và trả về danh sách province thông qua ApiResponse.
 *
 * Base URL: /api/provinces
 *
 * @author Phong
 * @version 1.0
 */
@RestController
@RequestMapping("/api/provinces")
@RequiredArgsConstructor
public class ProvinceController {

    private final ProvinceService provinceService;

    /**
     *
     * Chức năng: Lấy toàn bộ danh sách province trong hệ thống.
     *
     * Quy trình:
     * - Gọi ProvinceService để lấy danh sách province.
     * - Nhận dữ liệu đã được xử lý từ service layer.
     * - Wrap dữ liệu vào ApiResponse để đảm bảo format response thống nhất.
     * - Trả về HTTP 200 OK cho client.
     *
     * @return danh sách ProvinceResponse
     *
     * @author Phong
     * @version 1.0
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProvinceResponse>>> getAllProvinces() {

        List<ProvinceResponse> data = provinceService.getAllProvinces();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Get provinces successfully",
                        data
                )
        );
    }
}