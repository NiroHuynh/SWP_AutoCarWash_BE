package com.swp.autocarwash.servicepackage.service.impl;

import com.swp.autocarwash.servicepackage.entity.ServiceCategory;
import com.swp.autocarwash.servicepackage.repository.ServiceCategoryRepository;
import com.swp.autocarwash.servicepackage.service.ServiceCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    private final ServiceCategoryRepository repository;

    @Override
    public List<ServiceCategory> getAll() {
        return repository.findAll();
    }
}
