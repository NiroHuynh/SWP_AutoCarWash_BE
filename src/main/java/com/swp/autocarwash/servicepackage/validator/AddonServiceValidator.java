package com.swp.autocarwash.servicepackage.validator;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 *
 * Validator kiểm tra business rule của addon service
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class AddonServiceValidator {


    /**
     *
     * Validate danh sách addon id từ module khác gửi qua
     *
     * @param ids danh sách addon id
     *
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
     * Kiểm tra tất cả addon request đều tồn tại trong database
     *
     * @param requestIds id client gửi
     * @param existIds id tồn tại
     *
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
}
