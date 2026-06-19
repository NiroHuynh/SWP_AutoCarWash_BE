package com.swp.autocarwash.servicepackage.service.impl;

import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.servicepackage.mapper.AddonServiceMapper;
import com.swp.autocarwash.servicepackage.repository.AddonServiceRepository;
import com.swp.autocarwash.servicepackage.service.AddonServiceService;
import com.swp.autocarwash.servicepackage.validator.AddonServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


/**
 *
 * Implementation xử lý logic addon service
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



    /**
     *
     * Lấy toàn bộ addon service đang hoạt động
     *
     * @return danh sách addon contract
     *
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
     * Lấy addon service theo danh sách id
     *
     * @param ids danh sách addon id
     *
     * @return danh sách addon contract
     *
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
     * Tính tổng thời gian addon
     *
     * @param ids danh sách addon id
     *
     * @return tổng thời gian phút
     *
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
     * Tính tổng giá addon
     *
     * @param ids danh sách addon id
     *
     * @return tổng giá tiền
     *
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

}
