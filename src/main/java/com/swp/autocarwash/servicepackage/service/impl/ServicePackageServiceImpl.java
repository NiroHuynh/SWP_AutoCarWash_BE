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

    private final ServicePackageRepository servicePackageRepository;

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
     * Lấy tất cả package chưa xóa.
     * durationMinutes = SUM(addon.duration_minutes) qua package_addon_mapping.
     * Không có addon nào → 0.
     * Trả thêm danh sách addonIds của từng package.
     */
    @Override
    public List<ServicePackageResponse> getAllServicePackages() {

        List<Object[]> rows = repository.findAllWithDuration();

        List<Integer> packageIds = rows.stream()
                .map(row -> ((Number) row[0]).intValue())
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

        return rows.stream()
                .map(row -> {
                    Integer packageId = ((Number) row[0]).intValue();

                    return ServicePackageResponse.builder()
                            .id(packageId)
                            .name((String) row[1])
                            .description((String) row[2])
                            .basePrice((BigDecimal) row[3])
                            .durationMinutes(((Number) row[4]).intValue())
                            .addonIds(addonMap.getOrDefault(packageId, List.of()))
                            .build();
                })
                .toList();
    }

    /**
     * Tạo service package mới
     * Flow:
     * 1. Tìm tất cả addon theo ids — nếu số lượng không khớp → có addon không tồn tại/đã xóa
     * 2. Tính tổng duration từ addon → required_slot = totalDuration / 15
     * 3. Tạo service_package (category cố định, BE tự set)
     * 4. Tạo package_addon_mapping cho từng addon
     * 5. Trả response kèm durationMinutes
     */
    @Override
    @Transactional
    public ServicePackageResponse createServicePackage(CreateServicePackageRequest request) {

        List<Integer> addonIds = request.getAddonIds();

        // 1. Validate addon tồn tại và chưa xóa
        List<AddonService> addons =
                addonServiceRepository.findAllByIdInAndIsDeletedFalse(addonIds);

        if (addons.size() != addonIds.size()) {
            throw new BusinessException(ErrorCode.ADDON_SERVICE_NOT_FOUND);
        }

        // 2. Tính tổng duration → required_slot
        int totalDuration = addons.stream()
                .mapToInt(AddonService::getDurationMinutes)
                .sum();

        int requiredSlot = totalDuration / 15;

        ServiceCategory category = serviceCategoryRepository.findById(2)
                .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_CATEGORY_NOT_FOUND));

        // 3. Tạo service_package
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

        ServicePackage saved = servicePackageRepository.save(servicePackage);

        // 4. Tạo package_addon_mapping cho từng addon
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

        // 5. Trả response
        return ServicePackageResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .basePrice(saved.getBasePrice())
                .durationMinutes(totalDuration)
                .addonIds(request.getAddonIds())
                .build();
    }

    /**
     * Cập nhật service package.
     * Flow:
     * 1. Kiểm tra service package tồn tại và chưa bị xóa
     * 2. Validate tất cả addonServiceIds tồn tại và chưa bị xóa
     * 3. Tính tổng duration từ các add-on → required_slot = totalDuration / 15
     * 4. Cập nhật thông tin service_package (name, basePrice, description, requiredSlot)
     * 5. Xóa toàn bộ package_addon_mapping hiện tại của package
     * 6. Tạo lại package_addon_mapping theo danh sách addonServiceIds mới
     * 7. Trả response kèm durationMinutes
     */
    @Override
    @Transactional
    public ServicePackageResponse updateServicePackage(
            Integer servicePackageId,
            UpdateServicePackageRequest request) {

        // 1. Kiểm tra package tồn tại
        ServicePackage servicePackage = repository
                .findByIdAndIsDeletedFalse(servicePackageId)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));

        // 2. Validate addon
        List<AddonService> addons = addonServiceRepository
                .findByIdInAndIsDeletedFalse(request.getAddonIds());

        if (addons.size() != request.getAddonIds().size()) {
            throw new BusinessException(ErrorCode.ADDON_SERVICE_NOT_FOUND);
        }

        // 3. Tính duration
        int durationMinutes = addons.stream()
                .mapToInt(AddonService::getDurationMinutes)
                .sum();

        // 4. required_slot
        int requiredSlot = (int) Math.ceil(durationMinutes / 15.0);

        // 5. Update package
        servicePackage.setName(request.getName());
        servicePackage.setBasePrice(request.getBasePrice());
        servicePackage.setDescription(request.getDescription());
        servicePackage.setRequiredSlot(requiredSlot);

        repository.save(servicePackage);

        // 6. Xóa mapping cũ
        packageAddonMappingRepository.deleteByServicePackage_Id(servicePackageId);

        // 7. Thêm mapping mới
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

        // 8. Response
        return ServicePackageResponse.builder()
                .id(servicePackage.getId())
                .name(servicePackage.getName())
                .description(servicePackage.getDescription())
                .basePrice(servicePackage.getBasePrice())
                .durationMinutes(durationMinutes)
                .addonIds(request.getAddonIds())
                .build();
    }

    /**
     * Xóa mềm service package.
     * Flow:
     * 1. Kiểm tra service package tồn tại và chưa bị xóa
     * 2. Kiểm tra package có đang được subscription plan chưa bị xóa sử dụng hay không
     * 3. Nếu đang được sử dụng → throw SERVICE_PACKAGE_IN_USE
     * 4. Hợp lệ → cập nhật isDeleted = true
     */
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
