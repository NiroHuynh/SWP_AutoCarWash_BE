package com.swp.autocarwash.servicepackage.adapter;

import com.swp.autocarwash.booking.port.ServicePackagePort;
import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import com.swp.autocarwash.servicepackage.service.ServicePackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 *
 * Adapter thật cung cấp service package
 * cho module booking
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("pro")
@RequiredArgsConstructor
public class ServicePackageBookingAdapter implements ServicePackagePort {



    private final ServicePackageService service;




    /**
     *
     * Lấy toàn bộ package cho booking
     *
     * @return danh sách package contract
     *
     */
    @Override
    public List<ServicePackageContract> getAllPackages(){
        return service.getAll();
    }




    /**
     *
     * Lấy duration của package
     *
     * @param servicePackageId package id
     *
     */
    @Override
    public Integer getDuration(
            Integer servicePackageId
    ){
        return service.getDuration(
                servicePackageId
        );
    }





    /**
     *
     * Lấy chi tiết package
     *
     * @param id package id
     *
     */
    @Override
    public ServicePackageContract getServicePackage(
            Integer id
    ){
        return service.getById(id);
    }





    /**
     *
     * Lấy package theo id
     *
     */
    @Override
    public ServicePackageContract getById(
            Integer id
    ){

        return service.getById(id);

    }

}
