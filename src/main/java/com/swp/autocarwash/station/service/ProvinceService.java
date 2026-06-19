package com.swp.autocarwash.station.service;



import com.swp.autocarwash.station.dto.response.ProvinceResponse;

import java.util.List;

public interface ProvinceService {
    List<ProvinceResponse> getAllProvinces();
}
