package com.swp.autocarwash.servicepackage.service.impl;

import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.servicepackage.dto.response.ServicePackageResponse;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import com.swp.autocarwash.servicepackage.entity.enums.ServicePackageStatus;
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
 * Chức năng: ServicePackageServiceImpl triển khai các nghiệp vụ xử lý
 * liên quan đến service package trong hệ thống.
 *
 * Class này chịu trách nhiệm xử lý business logic của service package,
 * bao gồm lấy danh sách package, tìm package theo id và lấy thời lượng thực hiện.
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
     * Chức năng: Lấy danh sách toàn bộ service package đang hoạt động.
     *
     * Quy trình:
     * - Gọi repository lấy các service package chưa bị xóa mềm.
     * - Mapping dữ liệu từ ServicePackage entity sang ServicePackageContract.
     * - Trả về danh sách package hợp lệ trong hệ thống.
     *
     * @return danh sách ServicePackageContract
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<ServicePackage> getAll(){


        return repository
                .findByIsDeletedFalse();

    }





    /**
     *
     * Chức năng: Lấy thông tin chi tiết service package theo id.
     *
     * Quy trình:
     * - Validate service package id đầu vào.
     * - Truy vấn service package theo id và trạng thái active.
     * - Nếu không tồn tại thì throw BusinessException.
     * - Mapping entity sang ServicePackageContract.
     * - Trả về thông tin package.
     *
     * @param id service package id cần tìm
     *
     * @return ServicePackageContract chứa thông tin service package
     *
     * @author Phong
     * @version 1.0
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
     * Chức năng: Lấy thời gian thực hiện của service package.
     *
     * Quy trình:
     * - Nhận service package id.
     * - Lấy thông tin package thông qua phương thức getById().
     * - Lấy durationMinutes từ ServicePackageContract.
     * - Trả về thời gian thực hiện theo phút.
     *
     * @param id service package id cần lấy duration
     *
     * @return thời gian thực hiện service package tính theo phút
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public Integer getDuration(
            Integer id
    ){
        return getById(id)
                .getDurationMinutes();

    }

    @Override
    public List<ServicePackageResponse> getActiveServicePackages() {

        return repository
                .findAllByStatusAndIsDeletedFalse(ServicePackageStatus.ACTIVE)
                .stream()
                .map(servicePackage -> ServicePackageResponse.builder()
                        .id(servicePackage.getId())
                        .name(servicePackage.getName())
                        .basePrice(servicePackage.getBasePrice())
                        .description(servicePackage.getDescription())
                        .build())
                .toList();
    }
}
