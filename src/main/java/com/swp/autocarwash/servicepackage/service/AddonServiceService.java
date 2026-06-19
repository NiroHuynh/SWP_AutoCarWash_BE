package com.swp.autocarwash.servicepackage.service;

import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;

import java.math.BigDecimal;
import java.util.List;


/**
 *
 * Interface xử lý nghiệp vụ addon service
 *
 * @author Phong
 * @version 1.0
 */
public interface AddonServiceService {


    /**
     *
     * Lấy toàn bộ addon service
     *
     * @return danh sách addon
     *
     */
    List<AddonServiceContract> getAll();


    /**
     *
     * Lấy addon theo danh sách id
     *
     * @param ids danh sách id
     *
     * @return danh sách addon
     *
     */
    List<AddonServiceContract> getByIds(
            List<Integer> ids
    );


    /**
     *
     * Tính tổng thời gian addon
     *
     * @param ids danh sách addon
     *
     * @return tổng phút
     *
     */
    Integer calculateDuration(
            List<Integer> ids
    );


    /**
     *
     * Tính tổng tiền addon
     *
     * @param ids danh sách addon
     *
     * @return tổng tiền
     *
     */
    BigDecimal calculatePrice(
            List<Integer> ids
    );
}
