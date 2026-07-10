package com.swp.autocarwash.servicepackage.repository;

import com.swp.autocarwash.servicepackage.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Integer> {
}
