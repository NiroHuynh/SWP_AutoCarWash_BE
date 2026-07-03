package com.swp.autocarwash.servicepackage.adapter;

import com.swp.autocarwash.booking.dto.response.BookingContextResponse;
import com.swp.autocarwash.booking.port.ServicePackagePort;
import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import com.swp.autocarwash.servicepackage.service.ServicePackageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * Chức năng: ServicePackageBookingAdapter là adapter production triển khai
 * ServicePackagePort, đóng vai trò cầu nối giữa booking module và service package module.
 *
 * Class này expose các nghiệp vụ liên quan đến service package thông qua contract
 * để booking module có thể sử dụng mà không phụ thuộc trực tiếp vào module khác.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("pro")
@RequiredArgsConstructor
public class ServicePackageBookingAdapter implements ServicePackagePort {



    private final ServicePackageService service;

    private final ModelMapper modelMapper;



    /**
     *
     * Chức năng: Lấy danh sách toàn bộ service package phục vụ booking.
     *
     * Quy trình:
     * - Gọi ServicePackageService lấy danh sách package.
     * - Service xử lý truy vấn dữ liệu và mapping entity.
     * - Trả về danh sách ServicePackageContract.
     *
     * @return danh sách ServicePackageContract
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<BookingContextResponse.ServicePackageDTO> getAllPackages(){
        List<ServicePackage> servicePackageContracts = service.getAll();
        return convertToServicePackageDTOs(servicePackageContracts);
    }

    private List<BookingContextResponse.ServicePackageDTO>
                    convertToServicePackageDTOs( List<ServicePackage> servicePackageContracts){
        List<BookingContextResponse.ServicePackageDTO> servicePackageDTOs = new ArrayList<>();
        for(ServicePackage servicePackage : servicePackageContracts){
            BookingContextResponse.ServicePackageDTO servicePackageDTO = modelMapper.map(servicePackage,BookingContextResponse.ServicePackageDTO.class);
            servicePackageDTO.setDurationMinutes(servicePackage.getRequiredSlot()*15);
            List<Integer> addonServiceIds = getAddonIdsOfServicePackage(servicePackage);
            servicePackageDTO.setAddonServiceIds(addonServiceIds);
            servicePackageDTOs.add(servicePackageDTO);
        }
        return servicePackageDTOs;
    }

    private List<Integer> getAddonIdsOfServicePackage(ServicePackage servicePackage) {
        return servicePackage.getAddonServices().stream().map((item)-> item.getId()).toList();
    }


    /**
     *
     * Chức năng: Lấy thời gian thực hiện của một service package.
     *
     * Quy trình:
     * - Nhận servicePackageId cần lấy duration.
     * - Gọi ServicePackageService xử lý lấy thông tin package.
     * - Trả về thời lượng package theo phút.
     *
     * @param servicePackageId id của service package cần lấy duration
     *
     * @return thời gian thực hiện package (phút)
     *
     * @author Phong
     * @version 1.0
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
     * Chức năng: Lấy thông tin chi tiết service package theo id.
     *
     * Quy trình:
     * - Nhận servicePackageId.
     * - Gọi ServicePackageService tìm package tương ứng.
     * - Mapping dữ liệu sang ServicePackageContract.
     * - Trả về thông tin package.
     *
     * @param id id của service package cần lấy thông tin
     *
     * @return ServicePackageContract chứa thông tin package
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public ServicePackageContract getServicePackage(
            Integer id
    ){
        return service.getById(id);
    }





    /**
     *
     * Chức năng: Lấy service package theo id phục vụ flow booking.
     *
     * Quy trình:
     * - Nhận package id cần tìm.
     * - Gọi ServicePackageService truy vấn dữ liệu.
     * - Trả về ServicePackageContract.
     *
     * @param id id của service package
     *
     * @return ServicePackageContract tương ứng với id
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public ServicePackageContract getById(
            Integer id
    ){

        return service.getById(id);

    }

}
