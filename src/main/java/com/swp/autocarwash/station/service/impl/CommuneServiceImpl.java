package com.swp.autocarwash.station.service.impl;


import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.station.dto.response.CommuneResponse;
import com.swp.autocarwash.station.entity.Commune;
import com.swp.autocarwash.station.repository.CommuneRepository;
import com.swp.autocarwash.station.repository.ProvinceRepository;
import com.swp.autocarwash.station.service.CommuneService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class CommuneServiceImpl implements CommuneService {

    private final CommuneRepository communeRepository;
    private final ProvinceRepository provinceRepository;
    private final ModelMapper modelMapper;

    /**
     * Flow:
     * 1. Validate province tồn tại
     * 2. Query communes theo provinceId
     * 3. Map entity → DTO
     */
    @Override
    public List<CommuneResponse> getCommunesByProvinceId(Integer provinceId) {

        if (!provinceRepository.existsById(provinceId)) {
            throw new BusinessException(ErrorCode.PROVINCE_NOT_FOUND);
        }

        List<Commune> communes = communeRepository.findByProvinceId(provinceId);

        return communes.stream()
                .map(commune -> modelMapper.map(commune, CommuneResponse.class))
                .toList();
    }
}
