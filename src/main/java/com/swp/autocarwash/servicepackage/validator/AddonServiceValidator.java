package com.swp.autocarwash.servicepackage.validator;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.servicepackage.repository.AddonServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 *
 * Chức năng: AddonServiceValidator dùng để kiểm tra các business rule
 * liên quan đến addon service.
 *
 * Class này đảm nhiệm việc validate dữ liệu đầu vào từ các module khác,
 * đảm bảo addon service tồn tại và hợp lệ trước khi thực hiện nghiệp vụ.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class AddonServiceValidator {

    private final AddonServiceRepository addonServiceRepository;

    /**
     *
     * Chức năng: Kiểm tra danh sách addon id nhận từ module khác có hợp lệ hay không.
     *
     * Quy trình:
     * - Kiểm tra danh sách addon id có null hoặc rỗng.
     * - Duyệt qua từng id trong danh sách.
     * - Kiểm tra id có null hoặc nhỏ hơn / bằng 0.
     * - Nếu phát hiện dữ liệu không hợp lệ thì throw BusinessException.
     *
     * @param ids danh sách addon id cần validate
     *
     * @return không trả về giá trị, throw exception nếu dữ liệu không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    public void validateAddonIds(
            List<Integer> ids
    ){


        if(ids == null || ids.isEmpty()){

            throw new BusinessException(
                    ErrorCode.ADDON_SERVICE_INVALID
            );

        }


        boolean invalidId =
                ids.stream()
                        .anyMatch(
                                id -> id == null || id <= 0
                        );


        if(invalidId){

            throw new BusinessException(
                    ErrorCode.ADDON_SERVICE_INVALID
            );

        }

    }



    /**
     *
     * Chức năng: Kiểm tra toàn bộ addon id client gửi lên có tồn tại trong database.
     *
     * Quy trình:
     * - Nhận danh sách addon id từ request.
     * - Nhận danh sách addon id thực tế tồn tại trong database.
     * - Chuyển danh sách tồn tại thành Set để tối ưu việc kiểm tra.
     * - So sánh từng id request với danh sách tồn tại.
     * - Nếu có addon không tồn tại thì throw BusinessException.
     *
     * @param requestIds danh sách addon id được gửi từ client
     * @param existIds danh sách addon id tồn tại trong database
     *
     * @return không trả về giá trị, throw exception nếu có addon không tồn tại
     *
     * @author Phong
     * @version 1.0
     */
    public void validateAddonExist(
            List<Integer> requestIds,
            List<Integer> existIds
    ){


        Set<Integer> existSet =
                existIds.stream()
                        .collect(Collectors.toSet());


        boolean missing =
                requestIds.stream()
                        .anyMatch(
                                id -> !existSet.contains(id)
                        );


        if(missing){

            throw new BusinessException(
                    ErrorCode.ADDON_SERVICE_NOT_FOUND
            );

        }

    }


    /**
     * Validate khi CREATE — check trùng name với tất cả addon active
     */
    public void validateNameDuplicateForCreate(String name) {

        if (addonServiceRepository.existsByNameAndIsDeletedFalse(name.trim())) {
            throw new BusinessException(
                    ErrorCode.ADDON_NAME_ALREADY_EXISTS
            );
        }
    }


    /**
     * Validate khi UPDATE — check trùng name nhưng loại trừ chính addon đang sửa
     */
    public void validateNameDuplicateForUpdate(String name, Integer addonServiceId) {

        if (addonServiceRepository.existsByNameAndIsDeletedFalseAndIdNot(
                name.trim(), addonServiceId)) {
            throw new BusinessException(
                    ErrorCode.ADDON_NAME_ALREADY_EXISTS
            );
        }
    }
}
