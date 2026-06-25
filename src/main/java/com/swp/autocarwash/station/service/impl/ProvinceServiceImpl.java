package com.swp.autocarwash.station.service.impl;

import com.swp.autocarwash.station.dto.response.ProvinceResponse;
import com.swp.autocarwash.station.entity.Province;
import com.swp.autocarwash.station.repository.ProvinceRepository;
import com.swp.autocarwash.station.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProvinceResponse> getAllProvinces() {
        List<Province> provinces = provinceRepository.findAll();

        return provinces.stream()
                .map(province -> modelMapper.map(province, ProvinceResponse.class))
                .toList();
    }
}
