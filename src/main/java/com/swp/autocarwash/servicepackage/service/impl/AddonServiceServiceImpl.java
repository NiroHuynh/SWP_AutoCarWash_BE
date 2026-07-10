package com.swp.autocarwash.servicepackage.service.impl;

import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.servicepackage.dto.request.CreateAddonServiceRequest;
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

    private final ServiceCategoryRepository serviceCategoryRepository;

    private final AddonServiceMapper mapper;

    private final AddonServiceValidator validator;



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

    @Override
    @Transactional
    public AddonServiceContract create(CreateAddonServiceRequest request) {

        ServiceCategory serviceCategory = serviceCategoryRepository
                .findById(request.getServiceCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_SERVICE_CATEGORY));

        AddonService addonService = new AddonService();
        addonService.setId(repository.findMaxId() + 1);
        addonService.setName(request.getName());
        addonService.setPrice(request.getPrice());
        addonService.setDurationMinutes(request.getDurationMinutes());
        addonService.setServiceCategory(serviceCategory);
        addonService.setIsDeleted(false);

        return mapper.toContract(repository.save(addonService));
    }

}
