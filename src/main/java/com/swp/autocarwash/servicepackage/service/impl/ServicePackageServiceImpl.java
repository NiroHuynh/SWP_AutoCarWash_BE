package com.swp.autocarwash.servicepackage.service.impl;

import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.servicepackage.dto.request.CreateServicePackageRequest;
import com.swp.autocarwash.servicepackage.dto.request.UpdateServicePackageRequest;
import com.swp.autocarwash.servicepackage.dto.response.ServicePackageResponse;
import com.swp.autocarwash.servicepackage.entity.AddonService;
import com.swp.autocarwash.servicepackage.entity.PackageAddonMapping;
import com.swp.autocarwash.servicepackage.entity.ServiceCategory;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import com.swp.autocarwash.servicepackage.mapper.ServicePackageMapper;
import com.swp.autocarwash.servicepackage.repository.AddonServiceRepository;
import com.swp.autocarwash.servicepackage.repository.PackageAddonMappingRepository;
import com.swp.autocarwash.servicepackage.repository.ServiceCategoryRepository;
import com.swp.autocarwash.servicepackage.repository.ServicePackageRepository;
import com.swp.autocarwash.servicepackage.service.ServicePackageService;
import com.swp.autocarwash.servicepackage.validator.ServicePackageValidator;
import com.swp.autocarwash.subscription.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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


    private final AddonServiceRepository addonServiceRepository;

    private final PackageAddonMappingRepository packageAddonMappingRepository;

    private final ServiceCategoryRepository serviceCategoryRepository;

    private final SubscriptionPlanRepository subscriptionPlanRepository;


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

    /**
     * Lấy tất cả package chưa xóa
     * durationMinutes = required_slot * 15 (admin tự nhập)
     */
    @Override
    public List<ServicePackageResponse> getAllServicePackages() {

        List<ServicePackage> packages = repository.findByIsDeletedFalse();

        List<Integer> packageIds = packages.stream()
                .map(ServicePackage::getId)
                .toList();

        Map<Integer, List<Integer>> addonMap =
                packageAddonMappingRepository.findByServicePackageIdIn(packageIds)
                        .stream()
                        .collect(Collectors.groupingBy(
                                mapping -> mapping.getServicePackage().getId(),
                                Collectors.mapping(
                                        mapping -> mapping.getAddonService().getId(),
                                        Collectors.toList()
                                )
                        ));

        return packages.stream()
                .map(sp -> ServicePackageResponse.builder()
                        .id(sp.getId())
                        .name(sp.getName())
                        .description(sp.getDescription())
                        .basePrice(sp.getBasePrice())
                        .durationMinutes(sp.getRequiredSlot() * 15)
                        .addonIds(addonMap.getOrDefault(sp.getId(), List.of()))
                        .build()
                )
                .toList();
    }


    /**
     * Tạo service package mới
     * Flow:
     * 1. Validate name format + bội 15 + trùng name
     * 2. Validate addon tồn tại
     * 3. required_slot = durationMinutes / 15 (admin tự nhập duration)
     * 4. Tạo service_package
     * 5. Tạo package_addon_mapping
     * 6. Trả response
     */
    @Override
    @Transactional
    public ServicePackageResponse createServicePackage(CreateServicePackageRequest request) {

        // 1. Validate business rule
        validator.validateForCreate(request.getName(), request.getDurationMinutes());

        List<Integer> addonIds = request.getAddonIds();

        // 2. Validate addon tồn tại và chưa xóa
        List<AddonService> addons =
                addonServiceRepository.findByIdInAndIsDeletedFalse(addonIds);

        if (addons.size() != addonIds.size()) {
            throw new BusinessException(ErrorCode.ADDON_SERVICE_NOT_FOUND);
        }

        // 3. required_slot từ duration admin nhập
        int requiredSlot = request.getDurationMinutes() / 15;

        ServiceCategory category = serviceCategoryRepository.findById(2)
                .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_CATEGORY_NOT_FOUND));

        // 4. Tạo service_package
        ServicePackage servicePackage = ServicePackage.builder()
                .name(request.getName())
                .basePrice(request.getBasePrice())
                .description(request.getDescription())
                .serviceCategory(category)
                .requiredSlot(requiredSlot)
                .averageRating(BigDecimal.ZERO)
                .totalReviews(0)
                .isDeleted(false)
                .build();

        ServicePackage saved = repository.save(servicePackage);

        // 5. Tạo package_addon_mapping
        List<PackageAddonMapping> mappings = addonIds.stream()
                .map(addonId -> {
                    AddonService addon = addonServiceRepository.getReferenceById(addonId);
                    PackageAddonMapping mapping = new PackageAddonMapping();
                    mapping.setServicePackage(saved);
                    mapping.setAddonService(addon);
                    return mapping;
                })
                .toList();

        packageAddonMappingRepository.saveAll(mappings);

        // 6. Trả response
        return ServicePackageResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .basePrice(saved.getBasePrice())
                .durationMinutes(request.getDurationMinutes())
                .addonIds(request.getAddonIds())
                .build();
    }


    /**
     * Cập nhật service package
     * Flow:
     * 1. Validate name format + bội 15 + trùng name
     * 2. Kiểm tra package tồn tại
     * 3. Validate addon
     * 4. required_slot = durationMinutes / 15 (admin tự nhập)
     * 5. Update package
     * 6. Xóa mapping cũ → thêm mapping mới
     * 7. Trả response
     */
    @Override
    @Transactional
    public ServicePackageResponse updateServicePackage(
            Integer servicePackageId,
            UpdateServicePackageRequest request) {

        // 1. Validate business rule
        validator.validateForUpdate(request.getName(), request.getDurationMinutes(), servicePackageId);

        // 2. Kiểm tra package tồn tại
        ServicePackage servicePackage = repository
                .findByIdAndIsDeletedFalse(servicePackageId)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));

        // 3. Validate addon
        List<AddonService> addons = addonServiceRepository
                .findByIdInAndIsDeletedFalse(request.getAddonIds());

        if (addons.size() != request.getAddonIds().size()) {
            throw new BusinessException(ErrorCode.ADDON_SERVICE_NOT_FOUND);
        }

        // 4. required_slot từ duration admin nhập
        int requiredSlot = request.getDurationMinutes() / 15;

        // 5. Update package
        servicePackage.setName(request.getName());
        servicePackage.setBasePrice(request.getBasePrice());
        servicePackage.setDescription(request.getDescription());
        servicePackage.setRequiredSlot(requiredSlot);

        repository.save(servicePackage);

        // 6. Xóa mapping cũ → thêm mapping mới
        packageAddonMappingRepository.deleteByServicePackage_Id(servicePackageId);

        List<PackageAddonMapping> mappings = addons.stream()
                .map(addon -> PackageAddonMapping.builder()
                        .id(PackageAddonMapping.PackageAddonMappingKey.builder()
                                .servicePackageId(servicePackageId)
                                .addonServiceId(addon.getId())
                                .build())
                        .servicePackage(servicePackage)
                        .addonService(addon)
                        .build())
                .toList();

        packageAddonMappingRepository.saveAll(mappings);

        // 7. Response
        return ServicePackageResponse.builder()
                .id(servicePackage.getId())
                .name(servicePackage.getName())
                .description(servicePackage.getDescription())
                .basePrice(servicePackage.getBasePrice())
                .durationMinutes(request.getDurationMinutes())
                .addonIds(request.getAddonIds())
                .build();
    }


    @Override
    @Transactional
    public void deleteServicePackage(Integer servicePackageId) {

        ServicePackage servicePackage = repository
                .findByIdAndIsDeletedFalse(servicePackageId)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));

        boolean inUse = subscriptionPlanRepository
                .existsByServicePackage_IdAndIsDeletedFalse(servicePackageId);

        if (inUse) {
            throw new BusinessException(ErrorCode.SERVICE_PACKAGE_IN_USE);
        }

        servicePackage.setIsDeleted(true);
        repository.save(servicePackage);
    }
}