package com.swp.autocarwash.servicepackage.service.impl;

import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import com.swp.autocarwash.servicepackage.mapper.ServicePackageMapper;
import com.swp.autocarwash.servicepackage.repository.ServicePackageRepository;
import com.swp.autocarwash.servicepackage.service.ServicePackageService;
import com.swp.autocarwash.servicepackage.validator.ServicePackageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 *
 * Service xử lý logic service package
 *
 * @author Phong
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicePackageServiceImpl
        implements ServicePackageService {



    private final ServicePackageRepository repository;

    private final ServicePackageMapper mapper;

    private final ServicePackageValidator validator;




    /**
     *
     * Lấy toàn bộ service package active
     *
     * @return danh sách service package contract
     *
     */
    @Override
    public List<ServicePackageContract> getAll(){


        return repository
                .findByIsDeletedFalse()
                .stream()
                .map(mapper::toContract)
                .toList();

    }





    /**
     *
     * Lấy service package theo id
     *
     * @param id service package id
     *
     * @return service package contract
     *
     */
    @Override
    public ServicePackageContract getById(
            Integer id
    ){
        validator.validateId(id);

        ServicePackage entity =
                repository
                        .findByIdAndIsDeletedFalse(id)
                        .orElseThrow(
                                () -> new BusinessException(
                                        ErrorCode.SERVICE_PACKAGE_NOT_FOUND
                                )
                        );

        return mapper.toContract(entity);

    }





    /**
     *
     * Lấy thời gian thực hiện service package
     *
     * @param id service package id
     *
     * @return duration phút
     *
     */
    @Override
    public Integer getDuration(
            Integer id
    ){
        return getById(id)
                .getDurationMinutes();

    }


}
