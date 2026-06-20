package com.swp.autocarwash.servicepackage.service;

import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;

import java.util.List;


/**
 *
 * Chức năng: ServicePackageService định nghĩa các nghiệp vụ xử lý liên quan
 * đến service package trong hệ thống.
 *
 * Interface này cung cấp các phương thức lấy danh sách package, tìm package
 * theo id và lấy thời lượng thực hiện package phục vụ cho booking flow.
 *
 * @author Phong
 * @version 1.0
 */
public interface ServicePackageService {


    /**
     *
     * Chức năng: Lấy danh sách toàn bộ service package đang hoạt động.
     *
     * Quy trình:
     * - Gọi repository để truy vấn các service package hợp lệ.
     * - Lấy các package chưa bị xóa mềm.
     * - Mapping dữ liệu từ entity sang ServicePackageContract.
     * - Trả về danh sách package cho module sử dụng.
     *
     * @return danh sách ServicePackageContract
     *
     * @author Phong
     * @version 1.0
     */
    List<ServicePackageContract> getAll();



    /**
     *
     * Chức năng: Lấy thông tin chi tiết service package theo id.
     *
     * Quy trình:
     * - Nhận id của service package cần tìm.
     * - Kiểm tra package có tồn tại và đang hoạt động.
     * - Mapping dữ liệu sang ServicePackageContract.
     * - Trả về thông tin package.
     *
     * @param id service package id cần tìm
     *
     * @return ServicePackageContract chứa thông tin package
     *
     * @author Phong
     * @version 1.0
     */
    ServicePackageContract getById(
            Integer id
    );



    /**
     *
     * Chức năng: Lấy thời gian thực hiện của service package.
     *
     * Quy trình:
     * - Nhận service package id.
     * - Lấy thông tin required slot hoặc duration tương ứng.
     * - Chuyển đổi sang thời gian thực hiện tính theo phút.
     * - Trả về duration của package.
     *
     * @param id service package id cần lấy thời gian
     *
     * @return thời gian xử lý service package tính theo phút
     *
     * @author Phong
     * @version 1.0
     */
    Integer getDuration(
            Integer id
    );

}
