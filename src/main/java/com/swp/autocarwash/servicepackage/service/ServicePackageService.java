package com.swp.autocarwash.servicepackage.service;

import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;

import java.util.List;


/**
 *
 * Interface xử lý nghiệp vụ service package
 *
 * @author Phong
 * @version 1.0
 */
public interface ServicePackageService {


    /**
     *
     * Lấy tất cả service package
     *
     */
    List<ServicePackageContract> getAll();



    /**
     *
     * Lấy service package theo id
     *
     */
    ServicePackageContract getById(
            Integer id
    );



    /**
     *
     * Lấy thời gian xử lý service package
     *
     */
    Integer getDuration(
            Integer id
    );

}
