package com.swp.autocarwash.servicepackage.service.impl;

import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.servicepackage.dto.request.CreateAddonServiceRequest;
import com.swp.autocarwash.servicepackage.dto.request.UpdateAddonServiceRequest;
import com.swp.autocarwash.servicepackage.dto.response.AddonServiceResponse;
import com.swp.autocarwash.servicepackage.entity.AddonService;
import com.swp.autocarwash.servicepackage.entity.ServiceCategory;
import com.swp.autocarwash.servicepackage.mapper.AddonServiceMapper;
import com.swp.autocarwash.servicepackage.repository.AddonServiceRepository;
import com.swp.autocarwash.servicepackage.repository.ServiceCategoryRepository;
import com.swp.autocarwash.servicepackage.service.AddonServiceService;
import com.swp.autocarwash.servicepackage.validator.AddonServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


/**
 *
 * Chức năng: AddonServiceServiceImpl triển khai các nghiệp vụ xử lý addon service.
 *
 * Class này chịu trách nhiệm xử lý business logic liên quan đến addon service,
 * bao gồm lấy danh sách addon, tìm addon theo id, tính tổng thời gian và chi phí addon.
 *
 * @author Phong
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddonServiceServiceImpl implements AddonServiceService {


    private final AddonServiceRepository repository;

    private final AddonServiceMapper mapper;

    private final AddonServiceValidator validator;

    private final ServiceCategoryRepository serviceCategoryRepository;




    /**
     *
     * Chức năng: Lấy danh sách toàn bộ addon service đang hoạt động.
     *
     * Quy trình:
     * - Gọi repository truy vấn các addon chưa bị xóa mềm.
     * - Mapping danh sách AddonService entity sang AddonServiceContract.
     * - Trả về danh sách addon contract cho module sử dụng.
     *
     * @return danh sách AddonServiceContract đang hoạt động
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<AddonServiceContract> getAll(){


        return repository
                .findByIsDeletedFalse()
                .stream()
                .map(mapper::toContract)
                .toList();

    }



    /**
     *
     * Chức năng: Lấy thông tin các addon service theo danh sách id.
     *
     * Quy trình:
     * - Validate danh sách addon id đầu vào.
     * - Truy vấn database các addon tương ứng và chưa bị xóa.
     * - Mapping entity sang AddonServiceContract.
     * - Kiểm tra các addon trả về có đầy đủ theo yêu cầu.
     * - Trả về danh sách addon hợp lệ.
     *
     * @param ids danh sách id addon service cần lấy
     *
     * @return danh sách AddonServiceContract theo ids
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<AddonServiceContract> getByIds(
            List<Integer> ids
    ){


        validator.validateAddonIds(ids);



        List<AddonServiceContract> addons =
                repository
                        .findByIdInAndIsDeletedFalse(ids)
                        .stream()
                        .map(mapper::toContract)
                        .toList();



        validator.validateAddonExist(
                ids,
                addons.stream()
                        .map(AddonServiceContract::getId)
                        .toList()
        );


        return addons;

    }




    /**
     *
     * Chức năng: Tính tổng thời gian thực hiện của danh sách addon service.
     *
     * Quy trình:
     * - Nhận danh sách addon id.
     * - Lấy thông tin addon thông qua phương thức getByIds().
     * - Lấy durationMinutes của từng addon.
     * - Cộng tổng thời gian của tất cả addon.
     * - Trả về tổng thời gian tính theo phút.
     *
     * @param ids danh sách id addon service
     *
     * @return tổng thời gian addon service (phút)
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public Integer calculateDuration(
            List<Integer> ids
    ){


        return getByIds(ids)
                .stream()
                .mapToInt(
                        AddonServiceContract::getDurationMinutes
                )
                .sum();

    }




    /**
     *
     * Chức năng: Tính tổng chi phí của danh sách addon service.
     *
     * Quy trình:
     * - Nhận danh sách addon id.
     * - Lấy thông tin giá của từng addon.
     * - Cộng tổng giá trị các addon.
     * - Trả về tổng chi phí addon.
     *
     * @param ids danh sách id addon service cần tính giá
     *
     * @return tổng giá tiền addon service
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public BigDecimal calculatePrice(
            List<Integer> ids
    ){


        return getByIds(ids)
                .stream()
                .map(
                        AddonServiceContract::getPrice
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

    }

    /**
     * Lấy toàn bộ addon chưa xóa, map sang response
     */
    @Override
    public List<AddonServiceResponse> getAllAddonServices() {

        return repository
                .findByIsDeletedFalse()
                .stream()
                .map(addon -> AddonServiceResponse.builder()
                        .id(addon.getId())
                        .name(addon.getName())
                        .price(addon.getPrice())
                        .durationMinutes(addon.getDurationMinutes())
                        .description(addon.getDescription())
                        .build()
                )
                .toList();
    }

    @Override
    @Transactional
    public AddonServiceResponse createAddonService(CreateAddonServiceRequest request) {

        ServiceCategory category = serviceCategoryRepository.findById(1)
                .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_CATEGORY_NOT_FOUND));

        validator.validateFields(request.getName(), request.getPrice(), request.getDurationMinutes());
        validator.validateNameDuplicateForCreate(request.getName());

        // Tạo entity trực tiếp từ request
        AddonService addon = AddonService.builder()
                .name(request.getName())
                .price(request.getPrice())
                .durationMinutes(request.getDurationMinutes())
                .description(request.getDescription())
                .serviceCategory(category)
                .isDeleted(false)
                .build();

        AddonService saved = repository.save(addon);

        // Build response trực tiếp
        return AddonServiceResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .price(saved.getPrice())
                .durationMinutes(saved.getDurationMinutes())
                .description(saved.getDescription())
                .build();
    }

    /**
     * Cập nhật addon service
     * Flow:
     * 1. Tìm addon theo id — không tồn tại hoặc đã xóa mềm → SERVICE_001
     * 2. Update các field từ request (giữ nguyên id, isDeleted, serviceCategoryId)
     * 3. Save → trả response
     */
    @Override
    @Transactional
    public AddonServiceResponse updateAddonService(
            Integer addonServiceId,
            UpdateAddonServiceRequest request
    ) {

        // 1. Tìm addon, không có hoặc đã xóa → lỗi
        AddonService addon = repository
                .findById(addonServiceId)
                .filter(a -> !Boolean.TRUE.equals(a.getIsDeleted()))
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.ADDON_SERVICE_NOT_FOUND
                ));

        validator.validateFields(request.getName(), request.getPrice(), request.getDurationMinutes());
        validator.validateNameDuplicateForUpdate(request.getName(), addonServiceId);

        // 2. Update trực tiếp
        addon.setName(request.getName());
        addon.setPrice(request.getPrice());
        addon.setDurationMinutes(request.getDurationMinutes());
        addon.setDescription(request.getDescription());

        AddonService saved = repository.save(addon);

        // 3. Build response trực tiếp
        return AddonServiceResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .price(saved.getPrice())
                .durationMinutes(saved.getDurationMinutes())
                .description(saved.getDescription())
                .build();
    }

    /**
     * Xóa mềm addon service
     * Flow:
     * 1. Tìm addon → không có hoặc đã xóa → SERVICE_001
     * 2. Kiểm tra có service_package (active) nào đang dùng → SERVICE_003
     * 3. Set is_deleted = true → save
     */
    @Override
    @Transactional
    public void deleteAddonService(Integer addonServiceId) {

        // 1. Tìm addon, không có hoặc đã xóa mềm → lỗi
        AddonService addon = repository
                .findById(addonServiceId)
                .filter(a -> !Boolean.TRUE.equals(a.getIsDeleted()))
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.ADDON_SERVICE_NOT_FOUND
                ));

        // 2. Đang được service_package active sử dụng → không cho xóa
        if (repository.isUsedByActiveServicePackage(addonServiceId)) {
            throw new BusinessException(
                    ErrorCode.ADDON_SERVICE_IN_USE
            );
        }

        // 3. Soft-delete
        addon.setIsDeleted(true);
        repository.save(addon);
    }



}
