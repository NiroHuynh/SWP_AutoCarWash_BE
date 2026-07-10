package com.swp.autocarwash.servicepackage.validator;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.servicepackage.repository.AddonServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
     * Validate business rule cho create/update:
     * - Name không bắt đầu bằng số
     * - Duration phải là bội 15 (cho phép 0)
     * - Check trùng name
     *
     * (Các check rỗng/null/price đã do DTO annotation xử lý)
     */
    public void validateForCreate(String name, Integer durationMinutes) {

        validateNameFormat(name);
        validateDurationMultiple(durationMinutes);
        validateNameDuplicateForCreate(name);
    }

    public void validateForUpdate(String name, Integer durationMinutes, Integer addonServiceId) {

        validateNameFormat(name);
        validateDurationMultiple(durationMinutes);
        validateNameDuplicateForUpdate(name, addonServiceId);
    }


    /**
     * Name không được bắt đầu bằng số — AC-15.1.4
     */
    private void validateNameFormat(String name) {

        if (name != null && !name.trim().isEmpty()
                && Character.isDigit(name.trim().charAt(0))) {
            throw new BusinessException(ErrorCode.ADDON_NAME_INVALID);
        }
    }


    /**
     * Duration phải là bội 15 (cho phép 0) — AC-15.1.3
     */
    private void validateDurationMultiple(Integer durationMinutes) {

        if (durationMinutes != null && durationMinutes % 15 != 0) {
            throw new BusinessException(ErrorCode.ADDON_DURATION_INVALID);
        }
    }


    /**
     * Check trùng name khi create
     */
    private void validateNameDuplicateForCreate(String name) {

        if (addonServiceRepository.existsByNameAndIsDeletedFalse(name.trim())) {
            throw new BusinessException(ErrorCode.ADDON_NAME_ALREADY_EXISTS);
        }
    }


    /**
     * Check trùng name khi update — loại trừ chính nó
     */
    private void validateNameDuplicateForUpdate(String name, Integer addonServiceId) {

        if (addonServiceRepository.existsByNameAndIsDeletedFalseAndIdNot(
                name.trim(), addonServiceId)) {
            throw new BusinessException(ErrorCode.ADDON_NAME_ALREADY_EXISTS);
        }
    }
}
