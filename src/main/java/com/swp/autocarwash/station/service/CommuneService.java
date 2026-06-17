package com.swp.autocarwash.station.service;


import com.swp.autocarwash.station.dto.response.CommuneResponse;

import java.util.List;

public interface CommuneService {

    /**
     * Lấy danh sách commune theo provinceId
     * Purpose: business logic layer cho API communes
     */
    List<CommuneResponse> getCommunesByProvinceId(Integer provinceId);
}
